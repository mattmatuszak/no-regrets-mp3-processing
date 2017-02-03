package com.ec.nr.runners.script;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ec.nr.NREnvironment;
import com.ec.nr.runners.CleanupDirectories;
import com.ec.nr.runners.MP3Runnable;
import com.ec.nr.sheets.creds.MP3SpreadsheetService;
import com.ec.nr.sheets.creds.SpreadsheetDataRow;
import com.ec.nr.workq.WorkQManager;

@Component
public class RunScriptFactory {

	@Autowired private NREnvironment env;
	@Autowired private MP3SpreadsheetService spreadsheet;
	@Autowired private WorkQManager workQ;
	
	@Value( "${audio.config.leadin}" )
	private String LEADIN_FILE_NAME;
	
	@Value( "${audio.config.logo}" )
	private String LOGO_FILE_NAME;
	
	public MP3Runnable getScriptRunner(String status, String mp3Id) {
		
		switch(status) {
		
		// upload complete 
		
		case "Upload Complete":
			return new StraightCopyRunner
					(
							new DirectoryInfo(env.LANDING_PAD_DIR, env.LANDING_PAD_ARCHIVE_DIR, env.SCRIPTS_DIR, env.LOGS_DIR)
							, new ScriptInfo
							(
									"copy.sh" 
									, "Backup Original"
									, mp3Id
									, null//" -t " + env.USER_EDIT_DIR + "/" + mp3Id + ".mp3"
							)
							, spreadsheet
					);
		
		case "Backup Original Complete":
			return new ScriptProcessingRunner
					(
							new DirectoryInfo(env.LANDING_PAD_DIR, env.PRE_EDIT_DIR, env.SCRIPTS_DIR, env.LOGS_DIR)
							, new ScriptInfo("convertToMono.sh", "Convert To Mono", mp3Id, null)
							, spreadsheet
					);
		
		case "Convert To Mono Complete":
			return new CopyRunner
					(
							new DirectoryInfo(env.PRE_EDIT_DIR, env.USER_EDIT_DIR, env.SCRIPTS_DIR, env.LOGS_DIR)
							, new ScriptInfo
							(
									"copy.sh" 
									, "Copy To User Edit"
									, mp3Id
									, null//" -t " + env.USER_EDIT_DIR + "/" + mp3Id + ".mp3"
							)
							, spreadsheet
					);
		// After the 'Copy To User Edit Complete', we are assuming the editor will make their changes to the file...so there is a break in this pause
		case "X":
		case "x":
			return new ScriptProcessingRunner
					(
							new DirectoryInfo(env.USER_EDIT_DIR, env.POST_EDIT_DIR, env.SCRIPTS_DIR, env.LOGS_DIR)
							, new ScriptInfo("amplify.sh", "Amplify", mp3Id, null)
							, spreadsheet
					);
		case "Amplify Complete":
			return new ScriptProcessingRunner
					(
							new DirectoryInfo(env.POST_EDIT_DIR, env.POST_EDIT_DIR, env.SCRIPTS_DIR, env.LOGS_DIR)
							, new ScriptInfo
							(
									"addLeadIn.sh"
									, "Add Lead In"
									, mp3Id
									, " -l " + env.DATA_DIR + "/" + LEADIN_FILE_NAME
							)
							, spreadsheet
					);
			
		case "Add Lead In Complete":
			
			SpreadsheetDataRow audioDetails = spreadsheet.getAudioDetails(mp3Id);
			
			return new ScriptProcessingRunner
					(
							new DirectoryInfo(env.POST_EDIT_DIR, env.POST_EDIT_DIR, env.SCRIPTS_DIR, env.LOGS_DIR)
							, new ScriptInfo
							(
								"tagMP3.sh"
								, "Tag MP3"
								, mp3Id
								,  " -e '" + audioDetails.getFieldValue(SpreadsheetDataRow.Field.SEMINAR) + "'"
								 + " -l '" + audioDetails.getFieldValue(SpreadsheetDataRow.Field.CONFERENCE) + "'"
								 + " -r '" + audioDetails.getFieldValue(SpreadsheetDataRow.Field.SPEAKER) + "'"
								 + " -i " + env.DATA_DIR + "/" + LOGO_FILE_NAME
							)
							, spreadsheet
					);
		
		case "Tag MP3 Complete": 
			return new CopyRunner
					(
							new DirectoryInfo(env.POST_EDIT_DIR, env.FINAL_AUDIO_DIR, env.SCRIPTS_DIR, env.LOGS_DIR)
							, new ScriptInfo
							(
									"copy.sh"
									, "Copy To Final"
									, mp3Id
									, null
							)
							, spreadsheet
					);
			
		case "Copy To Final Complete":
			
			SpreadsheetDataRow audioDetailsForUpload = spreadsheet.getAudioDetails(mp3Id);
			
			return new CopyWithMeaningfulNameRunner
					(
							new DirectoryInfo(env.FINAL_AUDIO_DIR, env.UPLOAD_DIR, env.SCRIPTS_DIR, env.LOGS_DIR)
							, new ScriptInfo
							(
									"copy.sh"
									, "Copy To Upload"
									, mp3Id
									, null
							)
							, spreadsheet
							, audioDetailsForUpload.getFieldValue(SpreadsheetDataRow.Field.SEMINAR)
								+ " - " + audioDetailsForUpload.getFieldValue(SpreadsheetDataRow.Field.SPEAKER)
								+ " - " + mp3Id
					);
		
		case "Copy To Upload Complete":
			return new FTPRunner
					(
							new DirectoryInfo(env.USER_EDIT_DIR, env.FINAL_AUDIO_DIR, env.SCRIPTS_DIR, env.LOGS_DIR)
							, new ScriptInfo("ftp.sh", "FTP", mp3Id, " -s " + env.FINAL_AUDIO_DIR + "/" + mp3Id + ".mp3")
							, spreadsheet
					);
		
		case "Done":
		case "done":
		case "DONE":
			return new CleanupDirectories(spreadsheet, workQ, mp3Id, env);
		default:
			return null;
			
		}
		
	}
	
}
