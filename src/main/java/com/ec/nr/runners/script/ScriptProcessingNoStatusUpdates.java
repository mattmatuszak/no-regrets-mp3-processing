package com.ec.nr.runners.script;

import com.ec.nr.sheets.creds.MP3SpreadsheetService;

public class ScriptProcessingNoStatusUpdates extends ScriptProcessingRunner {

	protected ScriptProcessingNoStatusUpdates(
			DirectoryInfo directoryInfo
			, ScriptInfo info
			, MP3SpreadsheetService spreadsheet) {
		super(directoryInfo, info, spreadsheet);
	}
	
	
	@Override protected void updateStatus(String value) { }
	@Override protected void updateDuration(Long duration) { }
	
	
	

	
	
}
