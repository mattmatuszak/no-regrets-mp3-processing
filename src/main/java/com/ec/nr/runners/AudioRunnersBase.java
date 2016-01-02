package com.ec.nr.runners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.ec.nr.sheets.creds.SpeakerSpreadsheet;
import com.ec.nr.workq.WorkQManager;

public class AudioRunnersBase {

	@Value( "${audio.runners.base.scripts}" )
	protected String scriptsDirectory;
	@Value( "${audio.runners.base.working}" )
	protected String workingAudioDirectory;
	@Value( "${audio.runners.base.logs}" )
	protected String logsDirectory;
	@Value( "${audio.runners.base.landingpad}" )
	protected String landingPadDirectory;
	@Value( "${audio.runners.base.data}" )
	protected String dataDirectory;
	@Value( "${audio.runners.base.final}" )
	protected String finalAudioDirectory;
	
	@Autowired protected SpeakerSpreadsheet speakerSS;
	@Autowired protected WorkQManager workQManager;
	
	
	protected AudioRunnersBase() {
		
	}

	

	public SpeakerSpreadsheet getSpeakerSS() {
		return speakerSS;
	}

	public WorkQManager getWorkQManager() {
		return workQManager;
	}


	

}
