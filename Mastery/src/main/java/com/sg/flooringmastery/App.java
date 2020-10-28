/*
M4 Master - TSG Flooring Corp
Narish Singh
Date Created: 6/23/20
*/
package com.sg.flooringmastery;

import com.sg.flooringmastery.controller.Controller;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    
    public static void main(String[] args) {
        ApplicationContext actx = new ClassPathXmlApplicationContext("applicationContext.xml");
        
        Controller c = actx.getBean("controller", Controller.class);
        
        c.run();
    }
}
