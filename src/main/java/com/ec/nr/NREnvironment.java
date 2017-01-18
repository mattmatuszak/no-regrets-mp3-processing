package com.ec.nr;

import java.io.File;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NREnvironment {
	
	private static final Logger logger = LogManager.getLogger(NREnvironment.class);
	
	private static final String DEFAULT_DIR = "/tmp/nr";
	
	/**
	 * also known as upload folder
	 */
	@Value( "${MP3_LANDING_PAD_DIR:" + DEFAULT_DIR + "/landingpad}" ) 
	public String LANDING_PAD_DIR;
	
	@Value( "${MP3_LANDING_PAD_ARCHIVE_DIR:" + DEFAULT_DIR + "/landingpad-archive}" ) 
	public String LANDING_PAD_ARCHIVE_DIR;

	@Value( "${APP_SCRIPTS_DIR:" + DEFAULT_DIR + "/scripts}" )		
	public String SCRIPTS_DIR;
	
	@Value( "${APP_LOGS_DIR:" + DEFAULT_DIR + "/logs}" )			
	public String LOGS_DIR;
	
	@Value( "${APP_DATA_DIR:" + DEFAULT_DIR + "/data}" )			
	public String DATA_DIR;
	
	@Value( "${APP_FINAL_DIR:" + DEFAULT_DIR + "/final}" )			
	public String FINAL_AUDIO_DIR;
	
	@Value( "${APP_PRE_EDIT_DIR:" + DEFAULT_DIR + "/pre-edit}" )	
	public String PRE_EDIT_DIR;
	
	@Value( "${APP_EDIT_DIR:" + DEFAULT_DIR + "/edit}" )		
	public String USER_EDIT_DIR;
	
	@Value( "${APP_PRE_EDIT_DIR:" + DEFAULT_DIR + "/post-edit}" )	
	public String POST_EDIT_DIR;
	
	@Value( "${FTP_U:changeme}")			
	public String FTP_U;
	@Value( "${FTP_P:changeme}")			
	public String FTP_P;
	
	
	@Value("${SPREADSHEET_OAUTH2_DIR:" + DEFAULT_DIR + "/spreadsheet/oauth2}")
	public String SPREADSHEET_OAUTH2_DIR;
	
	@Value("${SPREADSHEET_OAUTH2_USER_INFO:" + DEFAULT_DIR + "/userInfo.json}")
	public String SPREADSHEET_OAUTH2_USER_INFO;
	
	
	@PostConstruct
	public void init() {
		initDirectory(LANDING_PAD_DIR);
		initDirectory(LANDING_PAD_ARCHIVE_DIR);
		initDirectory(SCRIPTS_DIR);
		initDirectory(LOGS_DIR);
		initDirectory(DATA_DIR);
		initDirectory(FINAL_AUDIO_DIR);
		initDirectory(PRE_EDIT_DIR);
		initDirectory(USER_EDIT_DIR);
		initDirectory(POST_EDIT_DIR);
		initDirectory(SPREADSHEET_OAUTH2_DIR);
	}
	
	private void initDirectory(String directoryName) {
		logger.info("Checking for directory: " + directoryName);
		File directory = new File(String.valueOf(directoryName));
	    if (! directory.exists()){
	        directory.mkdirs();
	        logger.info("Created directory: " + directoryName);
	    }
	}
	
	
}
 