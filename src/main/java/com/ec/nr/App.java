package com.ec.nr;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.ec.nr.watcher.LandingPadWatcher;
import com.ec.nr.watcher.SpreadsheetWatcher;
import com.ec.nr.watcher.WatcherManager;

@SpringBootApplication
public class App 
{
    public static void main( String[] args )
    {
    	Logger logger = LogManager.getLogger(App.class);
    	logger.info("starting NR 2015 processor!");
    	
    	ApplicationContext context = SpringApplication.run(App.class, args);
    	
//    	logger.info("Spring File:" + args[0]);
    	 
//    	ApplicationContext context = new FileSystemXmlApplicationContext("file:" + args[0]); //ClassPathXmlApplicationContext("/SpringBeans.xml");
    		// by loading our spring beans, we start our watchers!
    	
//    	WatcherManager watcherManager = (WatcherManager) context.getBean("WatcherManager");
    	
    	
/*    	boolean shutDown = false;
    	
    	while (!shutDown) {
    		
    		logger.info("Work Q Depth: " + watcherManager.getWorkQExecutor().getWorkQDepth());
    		try {
				Thread.currentThread().sleep(10000);
			} catch (InterruptedException e) {
				logger.error("thread interrupted...", e);
				shutDown = true;
			}
    		
    		//shutDown = System.in.read()
    	}
 */   	
    	/*logger.info("starting the MP3 watcher...");
    	MP3LandingPadWatcher mp3Watcher = (MP3LandingPadWatcher) context.getBean("MP3Watcher");
    	mp3Watcher.watch();
    	
    	logger.info("starting the spreadsheet watcher...");
    	SpreadsheetWatcher spreadsheetWatcher = (SpreadsheetWatcher) context.getBean("SpreadsheetWatcher");
    	spreadsheetWatcher.watch();
    	*/
    	
    }
}
