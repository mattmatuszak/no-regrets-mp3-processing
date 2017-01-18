package com.ec.nr;

import java.io.File;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import com.ec.nr.watcher.LandingPadWatcher;
import com.ec.nr.watcher.SpreadsheetWatcher;

@Configuration
@ComponentScan("com.ec.nr")
@EnableAutoConfiguration
public class NRMP3AppConfig {

	@Autowired NREnvironment nrEnvironment;
	
	@Bean
	public LandingPadWatcher landingPadWatcher() {
		return new LandingPadWatcher(nrEnvironment.LANDING_PAD_DIR);
	}
	
	
	@Value( "${spreadsheet.watcher.sleeptime}" )
	private int spreadsheetSleepTimeInterval;
	
	@Bean
	@Profile(value="live")
	public SpreadsheetWatcher spreadsheetWatcher() {
		return new SpreadsheetWatcher(spreadsheetSleepTimeInterval);
	}
	
	
	@Value("${MP3_PROCESSING_THREADS:3}")
	private int numberOfThreads;
	
	@Bean
	public ExecutorService executor() {
		return java.util.concurrent.Executors.newFixedThreadPool(numberOfThreads);
	}
	
	@Bean 
	public File credentialDirectory() {
		return new File(nrEnvironment.SPREADSHEET_OAUTH2_DIR);
	}
	
	
	
	
	
}
