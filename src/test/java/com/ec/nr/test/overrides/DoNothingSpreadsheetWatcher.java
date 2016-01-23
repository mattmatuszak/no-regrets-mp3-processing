package com.ec.nr.test.overrides;

import com.ec.nr.watcher.SpreadsheetWatcher;

public class DoNothingSpreadsheetWatcher extends SpreadsheetWatcher {

	
	public DoNothingSpreadsheetWatcher() {
		super(-1);
	}
	
	@Override
	public void run() {
		// do nothing
	}
}
