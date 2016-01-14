package com.ec.nr.runners;

import com.ec.nr.sheets.creds.SpeakerSpreadsheet;
import com.ec.nr.workq.WorkQManager;

public class AudioRunnersBase {
	
	private SpeakerSpreadsheet speakerSS;
	private WorkQManager workQManager;
	
	
	protected AudioRunnersBase(SpeakerSpreadsheet ss, WorkQManager wqm) {
		this.speakerSS = ss;
		this.workQManager = wqm;
	}

	

	public SpeakerSpreadsheet getSpeakerSS() {
		return speakerSS;
	}

	public WorkQManager getWorkQManager() {
		return workQManager;
	}


	

}
