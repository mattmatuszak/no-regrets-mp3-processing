package com.ec.nr.runners;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import com.ec.nr.NREnvironment;
import com.ec.nr.sheets.creds.MP3SpreadsheetService;
import com.ec.nr.workq.WorkQManager;

public class CleanupDirectories extends AbstractAudioRunnable {

	private NREnvironment env;
	
	public CleanupDirectories(MP3SpreadsheetService ss, WorkQManager wqm, String audioId, NREnvironment env) {
		super(ss, wqm, audioId);
		this.env = env;
	}

	@Override
	public void doWork() {
		
		//Iterator<File> files = 
		//		FileUtils.iterateFiles(new File(env.PRE_EDIT_DIR), new WildcardFileFilter("*" + getId() + "*.mp3"), null);
		
		//for (File file : files.) {
			
		//}
		
		//FileUtils.cleanDirectory(new File(env.PRE_EDIT_DIR));

	}

	@Override
	public String getStartingStatus() {
		return "Cleaning Up Directories";
	}

	@Override
	public String getEndingStatus() {
		return "Cleaning Up Directories Complete";
	}

}
