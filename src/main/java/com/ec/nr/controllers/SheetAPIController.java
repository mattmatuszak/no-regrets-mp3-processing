package com.ec.nr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ec.nr.sheets.creds.SheetConnectionSerivce;

@RestController
@RequestMapping("/api")
public class SheetAPIController {

	@Autowired SheetConnectionSerivce sheetConnService;
	
	@RequestMapping("/reconnect")
	public String reconnect() throws Exception {
		
		boolean result = sheetConnService.connect();
		return "Completed with result:" + result;
	}
	
	
	
	
}
