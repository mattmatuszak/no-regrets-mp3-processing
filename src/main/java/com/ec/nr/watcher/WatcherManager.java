package com.ec.nr.watcher;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ec.nr.workq.WorkQManager;

@Component
public class WatcherManager {

	@Autowired private SpreadsheetWatcher ssWatcher;
	@Autowired private LandingPadWatcher mp3LandingPadWatcher;
	@Autowired private WorkQManager workQManager;
	
	
	private WatcherManager() {
		
	}
	
	@PostConstruct
	private void startWatchers() {
		ssWatcher.setName("SS Watcher");
		ssWatcher.start();
		
		mp3LandingPadWatcher.setName("LP Watcher");
		mp3LandingPadWatcher.start();
	}

	public WorkQManager getWorkQExecutor() {
		return workQManager;
	}
	
	

}
