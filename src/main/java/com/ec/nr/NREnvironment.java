package com.ec.nr;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
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
	public void init() throws Exception {
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
		extractScripts();
		extractData();
	}
	
	private void extractData() throws Exception {
		
		FileUtils.cleanDirectory(new File(DATA_DIR));
		
		Resource[] dataFiles = 
				(new PathMatchingResourcePatternResolver()).getResources("classpath:/data/*.*");
			
			for (int dataFileIndex = 0; dataFileIndex < dataFiles.length; dataFileIndex++) {
				Resource dataFileFromApp = dataFiles[dataFileIndex];
				
				FileUtils.copyFile
				(
					  dataFileFromApp.getFile()
					, new File(DATA_DIR + "/" + dataFileFromApp.getFilename())
				);
			}
	}
	
	private void extractScripts() throws IOException, InterruptedException {
		
		FileUtils.cleanDirectory(new File(SCRIPTS_DIR));
		
		Resource[] scripts = 
			(new PathMatchingResourcePatternResolver()).getResources("classpath:/scripts/*.sh");
		
		for (int scriptResourceIndex = 0; scriptResourceIndex < scripts.length; scriptResourceIndex++) {
			Resource scriptFromApp = scripts[scriptResourceIndex];
			
			FileUtils.copyFile
			(
				  scriptFromApp.getFile()
				, new File(SCRIPTS_DIR + "/" + scriptFromApp.getFilename())
			);
		}
		
		String[] cmd = 
			{
				"/bin/sh"
				, "-c"
				, "chmod 554 " + SCRIPTS_DIR + "/*.sh " 
			};
		
		Process p = Runtime.getRuntime().exec(cmd);
		int returnCode = p.waitFor();
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
 