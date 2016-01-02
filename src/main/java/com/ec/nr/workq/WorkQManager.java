package com.ec.nr.workq;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ec.nr.runners.MP3Runnable;

@Service
public class WorkQManager {
	
	
	private static Logger logger = LogManager.getLogger(WorkQManager.class);
	
	@Autowired
	private ExecutorService executor;
	
	private HashMap<String, String> lockedMP3s = new HashMap<String, String>();


	private WorkQManager() {
		logger.info("Created WorkQExecutor");
	}
	
	public void addAudio(MP3Runnable runner) {
		
		if (lockedMP3s.containsKey(runner.getId())) {
			logger.debug("MP3 is already on the queue..." + runner.getId() + " ignoring.");
		} else {
			lockedMP3s.put(runner.getId(), runner.getClass().getName());
			executor.execute(runner);
		}
	}
	
	public void audioComplete(MP3Runnable runner) {
		if (lockedMP3s.containsKey(runner.getId())) {
			logger.debug("Removing from the queue..." + runner.getId() + ".");
			lockedMP3s.remove(runner.getId());
		} else {
			
			logger.error("MP3 is not on the queue...please investigate!..." + runner.getId());
		}
	}
	
	public boolean waitTillQueueIsDrained() {
		executor.shutdown();
		try {
			return executor.awaitTermination(60, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			logger.error("Problems waiting for LandingPadExecutor to shutdown!", e);
			return false;
		}
	}
	
	public void setExecutor(ExecutorService executor) {
		this.executor = executor;
	}
	
	public int getWorkQDepth() {
		return lockedMP3s.size();
	}

}
