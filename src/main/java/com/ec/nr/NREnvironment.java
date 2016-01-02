package com.ec.nr;

import org.springframework.beans.factory.annotation.Value;

public class NREnvironment {

	@Value( "${audio.runners.base.scripts}" )
	public String SCRIPTS_DIR;
	@Value( "${audio.runners.base.working}" )
	public String WORKING_DIR;
	@Value( "${audio.runners.base.logs}" )
	public String LOGS_DIR;
	@Value( "${audio.runners.base.landingpad}" )
	public String LANDING_PAD_DIR;
	@Value( "${audio.runners.base.data}" )
	public String DATA_DIR;
	@Value( "${audio.runners.base.final}" )
	public String FINAL_AUDIO_DIR;
}
