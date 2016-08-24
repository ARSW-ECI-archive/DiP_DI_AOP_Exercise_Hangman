###Escuela Colombiana de Ingeniería
###Arquitecturas de Software
####Programación Orientada por Aspectos


**Ejemplo básico**.


En este repositoroio están los fuentes y las librerías de una aplicación que está generando errores, para la cual, debido a las
malas prácticas de su programador en cuanto al manejo de excepciones (no propagarlas ni escalarlas), no se sabe a ciencia cierta cual es la causa (además, la causa parece estar en una de las librerías para las cuales no existen fuentes). Para poder identificar la causa, se le ha pedido que diseñe e implemente un aspecto que intercepte cualquier excepción lanzada por cualquier método, y que imprima su traza correspondiente. Algo a favor es que la aplicación está montada alrededor del framework Spring, de manera que es posible utilizar las facilidades de AOP que vienen incluidas en el mismo.

1. Revise en el archivo de configuración de Spring los Beans definidos y sus dependencias.
2. Cree un nuevo Bean, que se encargue de manejar los consejos (‘advice’) de los aspectos que se definan.
3. En dicho Bean agregue un método que reciba por parámetro un objeto de tipo JoinPoint y otro de tipo Exception. Tenga en cuenta el nombre asignado a dicho parámetro, pues éste se usará en la configuración del aspecto.
4. Basado en la siguiente plantilla para la configuración de aspectos, defina un aspecto que se active cuando se arroje una excepción. Haga que como consejo (‘advice’) se imprima la traza de la excepción.

	```java
	<aop:config>
		<aop:aspect ref="bean_aspectos">
			<aop:pointcut expression="expresión del poincut" id="identif.pointcut"/>
		
		<aop:after-throwing throwing="nombre_var_excepcion" pointcut-ref="identif.pointcut" method="metodo_advice"/>	
		</ aop:aspect>
			<aop:aspect ref="bean_aspectos2">
			...
		</ aop:aspect>
	</aop:config>
```


5. Puede encontrar mas información sobre programación orientada a aspectos y las expresiones de los pointcut la [documentación oficial de Spring](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/aop.html).


6. Pruebe el programa e intente identificar la causa del problema con los nuevos resultados obtenidos. Ahora, se quiere identificar si el método ‘execute’ de la clase MainProcessor se ejecuta un número determinado de veces a través de un aspecto, de manera que no
se haga interferencia en el código existente. Sin embargo, para hacer más flexible el uso de este aspecto, se quiere que el mismo tenga como punto de corte (‘pointcut’) aquellos métodos que tengan una anotación en particular. Por otro lado, dicho aspecto tendrá como consejo (‘advice’) realizar el conteo del número de invocaciones e imprimir un mensaje en cuanto el límite de número de invocaciones indicado se alcance. Para hacer esto:

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