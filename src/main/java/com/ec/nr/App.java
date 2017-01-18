package com.ec.nr;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
public class App 
{
    public static void main( String[] args )
    {
    	Logger logger = LogManager.getLogger(App.class);
    	logger.info("starting NR 2017 processor!");
    	
    	ApplicationContext context = SpringApplication.run(App.class, args);
    	
    }
}
