###Escuela Colombiana de Ingeniería
###Arquitecturas de Software
####Programación Orientada por Aspectos


#####Parte I. Ejemplo básico


En este repositoroio están los fuentes y las librerías de una aplicación que está generando errores, para la cual, debido a las
malas prácticas de su programador en cuanto al manejo de excepciones (no propagarlas ni escalarlas), no se sabe a ciencia cierta cual es la causa (además, la causa parece estar en una de las librerías para las cuales no existen fuentes). Para poder identificar la causa, se le ha pedido que diseñe e implemente un aspecto que intercepte cualquier excepción lanzada por cualquier método, y que imprima su traza correspondiente. Algo a favor es que la aplicación está montada alrededor del framework Spring, de manera que es posible utilizar las facilidades de AOP que vienen incluidas en el mismo.

1. Revise en el archivo de configuración de Spring los Beans definidos y sus dependencias.
2. Cree un nuevo Bean, que se encargue de manejar los consejos (‘advice’) de los aspectos que se definan.
3. En dicho Bean agregue un método que reciba por parámetro un objeto de tipo JoinPoint y otro de tipo Exception. Tenga en cuenta el nombre asignado a dicho parámetro, pues éste se usará en la configuración del aspecto.
4. Teniendo en cuenta que en el método anterior se usará como 'consejo' (Advice) del aspecto, y que el mismo recibirá la excepción interceptada, haga que en éste se muestre la traza de la misma.
4. Basado en la siguiente plantilla para la configuración de aspectos, defina un aspecto que se active cuando se arroje una excepción. Haga que como consejo (‘advice’) se imprima la traza de la excepción.

	```xml
	<aop:config>
		<aop:aspect ref="bean_aspectos">
		
			<!-- Pointcut -->
			<aop:pointcut expression="expresión del poincut" id="identif.pointcut"/>

			<!-- Advice & Context (after, around, after throwing, etc) -->				
			<aop:after-throwing throwing="nombre_var_excepcion" pointcut-ref="identif.pointcut" method="metodo_advice"/>
			
			<!-- <aop:before pointcut-ref="xxxxx" method="yyyy"/>-->
			<!-- <aop:after pointcut-ref="xxxxx" method="yyyy"/>-->
				
		</ aop:aspect>
		
		<aop:aspect ref="bean_aspectos2">
			...
		</ aop:aspect>
	</aop:config>
```


5. Puede encontrar mas información sobre programación orientada a aspectos y las expresiones de los pointcut la [documentación oficial de Spring](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/aop.html) (en la misma busque la sección que inicia con '*Some examples of common pointcut expressions are given below*'.


6. Pruebe el programa e intente identificar la causa del problema con los nuevos resultados obtenidos. 


7. Ahora, se quiere identificar si el método ‘execute’ de la clase MainProcessor se ejecuta un número determinado de veces a través de un aspecto, pero sin 'contaminar' el código existente. Sin embargo, para hacer más flexible el uso de este aspecto, se quiere que el mismo tenga como punto de corte (‘pointcut’) aquellos métodos que tengan una anotación en particular. Por otro lado, dicho aspecto tendrá como consejo (‘advice’) realizar el conteo del número de invocaciones e imprimir un mensaje en cuanto el límite de número de invocaciones indicado se alcance. Para hacer esto:

	1. Implemente una nueva anotación para métodos ( @Target(value=ElementType.METHOD) )
y con una retención de tipo “Runtime” (@Retention(RetentionPolicy.RUNTIME));
	2. A la anotación agregue un atributo de tipo entero, que sirva para parametrizar el número de ejecuciones que se va a validar.
	3. A la clase que maneja los aspectos, agregue un método para implementar el consejo del nuevo aspecto. En este caso, dicho método sólo debe recibir como parámetro un objeto de tipo JoinPoint. Haga que este método extraiga del objeto JoinPoint el método interceptado (objeto Method), y luego (usando el API de Reflection) extraiga la anotación, de dicha anotación extraiga el valor, y con este identifique si el número de ejecuciones límite se ha identificado.
Tenga también en cuenta que varios métodos podrían tener la misma
anotación, por lo que el método de joinPoint de alguna manera debe llevar cuentas separadas para cada método.


		Nota: para extraer el objeto Method del joinPoint:
		
		```java		
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Method method = signature.getMethod();
		```
		
	4. En el archivo de configuración de Spring agregue un nuevo aspecto donde:
	
		* El punto de corte será: “todos aquellos métodos que tengan la anotación creada anteriormente”. La expresión para el mismo será de la forma: @annotation(aaa.bbb.ccc.MiAnotacion)
		* El consejo será de tipo \<aop:before\>, \<aop:after\> o \<aop:around\>:
		 
			```xml			
			<aop:after pointcut-ref="identif.pointcut" method="metodo_advice"/>			
			```

	5. Agregue un nuevo método en la clase MainProcesor.
	6. Pruebe el funcionamiento del nuevo aspecto agregando las anotaciones al método creado en el punto 5 y al método execute de la clase MainProcessor con:
	
		* Límite de 10 invocaciones
		* Límite de 15 invocaciones

		
#####Parte II. Para la próxima clase.

Se le ha pedido que para el ejercicio desarrollado anteriormente (Hangman), se implemente un mecanismo que recoja datos del comportamiento de los jugadores para investigación en temas de neurología. Sin embargo, se quiere que este mecanismo de recolección de datos sea por completo desacoplado al código existente, pues la idea es poder remover el mismo con facilidad -sin tener que tocar el código- una vez se tengan los datos.

Se requiere entonces definir una serie de aspectos que permita:

1. Interceptar cada ensayo realizado por el usuario (la elección de una letra), si el mismo fue exitoso o no, y el tiempo tomado entre elección y elección.
2. Cada vez el juego es reiniciado (bien sea porque se adivinó la palabra o porque se ahorcó), consolidar los datos de la partida calculando:
	* El promedio de tiempo, en milisegundos, entre intento e intento.
	* El número de letras acertadas.

Por ahora, se tienen identificados dos mecanismos (y eventualmente más en el futuro) a través de los cuales se enviará la información a quienes quieren hacer investigación con los resultados:

1. Imprimiendo por consola los resultados, cuando el juego sea usado en el mismo sitio de la investigación.
2. Enviando los resultados por correo electrónico cuando el sujeto que hace la prueba trabaja remotamente. Para este, puede basarse [ejemplos de javamail existentes](https://github.com/PDSW-ECI/javamail), y usando como destino del correo (para probar su funcionamiento) el servicio de [MailTrap](https://mailtrap.io/).




Para todo lo anterior tenga en cuenta:

- Para crear sus aspectos, primero debe identificar qué operaciones debe interceptar, y así poder definir los	Puntos de Corte (pointcut).
- Se va a usar el mecanismo de 'tejido' basado en _proxies_ dinámicos, lo cual requiere que los métodos interceptados estén definidos en clases que tengan la categoría de _Bean_. Es decir, eventualmente tendrá que ajustar el esquema de inversión de dependencias - inyección de dependencias del ejercicio anterior.
- Como en este caso el consejo (Advice) del Aspecto a ser desarrollado tienen un comportamiento -a partir de los datos recogidos- que puede variar (dos en principio), se debe aplicar al mismo el principio de Inversión de Dependencias y la configuración para la Inyección de dependencias que corresponda.


	Tip 1: Recuerde incluír las dependencias necesarias para habilitar el soporte a AOP.

	Tip 2: Para este ejercicio vale la pena que revise [en la documentación oficial de Spring](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/aop.html) el uso de 'consejos' de tipo *Around*.

	
##Criterios de evaluación

1. Parte I.
	* Diseño:
		* Se creó un aspecto que tiene como Pointcut una anotación, que tiene como 'consejo' (de tipo antes o después) el llevar una cuenta del número de invocaciones, y que muestra un aviso cuando se supera el valor dado como propiedad de la anotación antes mencionada. Se debe tener en cuenta que la anotación puede están en diferentes métodos, por lo que se deben llevar cuentas diferentes por cada uno de éstos (si se lleva una cuenta global, se evalúa como R).
	* Funcional: 
		* Se muestra la traza completa de la excepción cuando se analizan números negativos y se lanza la alerta cuando se supera el número de invocaciones.

2. Parte II.
	* Diseño:
		* Se creó un aspecto que intercepta la realización de cada intento en el juego y que, como 'consejo', captura los resultados del mismo.
		* Se creó un aspecto que intercepta la finalizaición del juego y que, como 'consejo', consolida y procesa los resultados.
		* El Bean que tiene asociado el 'consejo' del aspecto antes mencionado cumple con el principio de inversión de dependencias al tener asociada una abstracción para el procesamiento de los datos. La configuración de la aplicación debe permitir inyectar al aspecto un manejador de los resultados.
	* Funcionalidad (se debe conservar la funcioanlidad anterior):
		* Según la configuración que se tenga, el juego debe permitir:
			* Mostrar las estadísticas del juego por consola.
			* Enviar las estadísticas del juego a un correo electrónico.
			

