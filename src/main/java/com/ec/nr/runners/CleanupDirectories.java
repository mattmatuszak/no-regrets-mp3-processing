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
	
	private void cleanupDirectory(String directory) {
		Iterator<File> files = 
				FileUtils.iterateFiles(new File(directory), new WildcardFileFilter("*" + getId() + "*"), null);
		
		while (files.hasNext()) {
			File file = (File) files.next();
			file.delete();
		}
	}

	@Override
	public void doWork() {
		
		cleanupDirectory(env.PRE_EDIT_DIR);
		cleanupDirectory(env.POST_EDIT_DIR);
		cleanupDirectory(env.USER_EDIT_DIR);
		cleanupDirectory(env.LANDING_PAD_DIR);
		cleanupDirectory(env.LOGS_DIR);
		cleanupDirectory(env.UPLOAD_DIR);
		
		//FileUtils.cleanDirectory(new File(env.PRE_EDIT_DIR));

	}

	@Override
	public String getStartingStatus() {
		return "Cleaning Up Directories";
	}

	@Override
	public String getEndingStatus() {
		return "Available To Public";
	}

}
