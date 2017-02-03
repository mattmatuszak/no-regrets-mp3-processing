package com.ec.nr.runners.script;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ec.nr.sheets.creds.MP3SpreadsheetService;

public class CopyWithMeaningfulNameRunner extends CopyRunner {
	
	private static Logger logger = LogManager.getLogger(CopyWithMeaningfulNameRunner.class);

	private String meaningfulName;
	
	protected CopyWithMeaningfulNameRunner(DirectoryInfo dirInfo
			, ScriptInfo info
			, MP3SpreadsheetService spreadsheet
			, String meaningfulName) {
		super(dirInfo, info, spreadsheet);
		logger.info("Meaningful name:" + meaningfulName);
		this.meaningfulName = meaningfulName;
		logger.info("Meaningful name:" + this.meaningfulName);
		setupCurrentAndNextFiles(-1);
	}

	
	@Override
	protected void setupCurrentAndNextFiles(int fileIndex) {
		logger.info("Meaningful name from setup:" + this.meaningfulName);
		setCurrentFile(new File(getDirectoryInfo().getSourceDirectory() + "/" + getId() + ".mp3"));
		setNextFile(new File("\"" + getDirectoryInfo().getWorkingDirectory() + "/" + meaningfulName + ".mp3\""));
	}
}
