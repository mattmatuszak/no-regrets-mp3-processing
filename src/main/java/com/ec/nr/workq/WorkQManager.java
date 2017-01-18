package com.ec.nr.workq;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ec.nr.runners.MP3Runnable;

@Service
public class WorkQManager {
	
	
	private static Logger logger = LogManager.getLogger(WorkQManager.class);
	
	@Autowired private ExecutorService executor;
	
	private WorkQManager() {
		logger.info("Created WorkQExecutor");
	}
	
	public void addAudio(MP3Runnable runner) {
		
			executor.execute(runner);
	}
	
	public void audioComplete(MP3Runnable runner) {
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
		
	public int getWorkQDepth() {
		return -1;
	}

}
