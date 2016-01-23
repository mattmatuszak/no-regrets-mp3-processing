package com.ec.nr.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.ec.nr.test.overrides.DoNothingSpreadsheetWatcher;
import com.ec.nr.watcher.SpreadsheetWatcher;

@Configuration
public class TestConfig {
	
	@Bean
	@Profile(value="test")
	public SpreadsheetWatcher spreadsheetWatcher() {
		return new DoNothingSpreadsheetWatcher();
	}
	
}
