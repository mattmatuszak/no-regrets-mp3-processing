package com.ec.nr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ec.nr.NREnvironment;
import com.ec.nr.runners.script.DirectoryInfo;
import com.ec.nr.runners.script.ScriptInfo;
import com.ec.nr.runners.script.ScriptProcessingRunner;
import com.ec.nr.sheets.creds.MP3SpreadsheetService;

@RestController
public class AdhocController {

	@Autowired private NREnvironment env;
	@Autowired private MP3SpreadsheetService spreadsheet;
	
	@RequestMapping("/runstats")
	public String runstats(@RequestParam(value="name", defaultValue="World") String mp3Id) {
		
		
		
		/*return new ScriptProcessingRunner
				(
						new DirectoryInfo(env.LANDING_PAD_DIR, env.PRE_USER_EDIT_DIR, env.SCRIPTS_DIR, env.LOGS_DIR)
						, new ScriptInfo("convertToMono.sh", "Convert To Mono", mp3Id, null)
						, spreadsheet
				);
		*/
		
		
		return "Success";
	}
	
}
