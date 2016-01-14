package com.ec.nr;

import java.io.File;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.ec.nr.watcher.LandingPadWatcher;
import com.ec.nr.watcher.SpreadsheetWatcher;

@Configuration
@PropertySource("classpath:nr.properties")
@ComponentScan("com.ec.nr")
public class NRMP3AppConfig {

	
	
	
	
	@Value( "${mp3landingpad.watcher.directory}" )
	private String pathToWatch;
	
	@Bean
	public LandingPadWatcher landingPadWatcher() {
		return new LandingPadWatcher(pathToWatch);
	}
	
	
	@Value( "${spreadsheet.watcher.sleeptime}" )
	private int spreadsheetSleepTimeInterval;
	
	@Bean
	public SpreadsheetWatcher spreadsheetWatcher() {
		return new SpreadsheetWatcher(spreadsheetSleepTimeInterval);
	}
	
	
	@Value("${WorkQThreadQExecutor.numberOfThreads}")
	private int numberOfThreads;
	
	@Bean
	public ExecutorService executor() {
		return java.util.concurrent.Executors.newFixedThreadPool(numberOfThreads);
	}
	
	
	@Value("${speaker.spreadsheet.credentialDirectory}")
	private String credentialDirectoryName;
	
	@Bean 
	public File credentialDirectory() {
		return new File(credentialDirectoryName);
	}
	
	
	@Bean
	public NREnvironment nrEnvironment() {
		return new NREnvironment();
	}
	
	
}
