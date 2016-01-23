package com.ec.nr.runners;

import com.ec.nr.sheets.creds.MP3SpreadsheetService;
import com.ec.nr.workq.WorkQManager;

public class AudioRunnersBase {
	
	private MP3SpreadsheetService speakerSS;
	private WorkQManager workQManager;
	
	
	protected AudioRunnersBase(MP3SpreadsheetService ss, WorkQManager wqm) {
		this.speakerSS = ss;
		this.workQManager = wqm;
	}

	

	public MP3SpreadsheetService getSpeakerSS() {
		return speakerSS;
	}

	public WorkQManager getWorkQManager() {
		return workQManager;
	}


	

}
