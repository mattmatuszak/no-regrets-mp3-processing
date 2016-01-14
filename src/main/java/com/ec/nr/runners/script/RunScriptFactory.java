package com.ec.nr.runners.script;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ec.nr.NREnvironment;
import com.ec.nr.sheets.creds.SpeakerSpreadsheet;
import com.ec.nr.sheets.creds.SpreadsheetDataRow;

@Component
public class RunScriptFactory {

	@Autowired private NREnvironment env;
	@Autowired private SpeakerSpreadsheet spreadsheet;
	
	@Value( "${audio.config.leadin}" )
	private String LEADIN_FILE_NAME;
	
	@Value( "${audio.config.logo}" )
	private String LOGO_FILE_NAME;
	
	public ScriptProcessingRunner getScriptRunner(String status, String mp3Id) {
		
		switch(status) {
		
		// upload complete 
		case "UPC":
		case "upc":
			return new ScriptProcessingRunner(env, new ScriptInfo("convertToMono.sh", "Convert To Mono", mp3Id, null), spreadsheet);
		
		case "Convert To Mono Complete":
			return new ScriptProcessingRunner(env, new ScriptInfo("amplify.sh", "Amplify", mp3Id, null), spreadsheet);
			
		case "Amplify Complete":
			return new ScriptProcessingRunner(env, new ScriptInfo("removeSilence.sh", "Remove Silence", mp3Id, null), spreadsheet);
		
		case "Remove Silence Complete":
			return new ScriptProcessingRunner(env, new ScriptInfo("addLeadIn.sh", "Add Lead In", mp3Id, " -l " + env.DATA_DIR + "/" + LEADIN_FILE_NAME), spreadsheet);
			
		case "Add Lead In Complete":
			
			SpreadsheetDataRow audioDetails = spreadsheet.getAudioDetails(mp3Id);
			
			return new ScriptProcessingRunner
					(
							env
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
			return new CopyToFinalRunner
					(
							env
							, new ScriptInfo
							(
									"copyToFinal.sh"
									, "Copy To Final"
									, mp3Id
									, " -t " + env.FINAL_AUDIO_DIR + "/" + mp3Id + ".mp3"
							)
							, spreadsheet
					);
		
		case "Copy To Final Complete":
			return new FTPRunner(env, new ScriptInfo("ftp.sh", "FTP", mp3Id, " -s " + env.FINAL_AUDIO_DIR + "/" + mp3Id + ".mp3"), spreadsheet);
			
		default:
			return null;
			
		}
		
	}
	
}
