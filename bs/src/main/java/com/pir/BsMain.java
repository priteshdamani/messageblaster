package com.pir;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created by pritesh on 12/13/13.
 */
public class BsMain {
    public static void main(String[] args) throws IOException {

        System.out.println("Starting --MASTER--  BACKSTAGE SERVER");

        ApplicationContext ac =  new ClassPathXmlApplicationContext(
                "spring-application-context-core.xml",
                "spring-cache.xml",
                "spring-datasource.xml",
                "spring-application-context-bs.xml"
                );

        System.out.println("Started --MASTER--  BACKSTAGE SERVER");

    }
}
