package test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Main {

	public static void main(String[] args) {
		
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
		MainProcessor mp=ac.getBean(MainProcessor.class);
		
		
		for (int i=0;i<100;i++){
			System.out.println(mp.execute(new int[]{1,2,3,4,5,6,10-i}));	
		}
		
		
		
		
		
	}
	
}
