package com.ec.nr.sheets.creds;

import java.util.Map;

public interface MP3SpreadsheetService {

	public Map<String, String> getAllMP3Statuses();

	public void updateField(String id, SpreadsheetDataRow.Field field, String value);

	public SpreadsheetDataRow getAudioDetails(String mp3ToRetrieve);

}