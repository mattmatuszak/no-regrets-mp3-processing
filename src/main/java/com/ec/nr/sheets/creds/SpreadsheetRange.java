package com.ec.nr.sheets.creds;

import java.util.Map;

import com.ec.nr.sheets.creds.SpreadsheetDataRow.Field;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SpreadsheetRange {

	private final String spreadsheetRange;
	private final Map<Field, ColumnIndexLetter> fieldToColumnLetterMap;
	
	public SpreadsheetRange(String spreadsheetRange, Map<Field, ColumnIndexLetter> fieldToColumnLetterMap) {
		this.spreadsheetRange = spreadsheetRange;
		this.fieldToColumnLetterMap = fieldToColumnLetterMap;
	}
	
	public String getSpreadsheetRange() {
		return spreadsheetRange;
	}

	public Map<Field, ColumnIndexLetter> getFieldToColumnLetterMap() {
		return fieldToColumnLetterMap;
	}

	public static class ColumnIndexLetter {
    	private String letter;
    	private int column;
    	public ColumnIndexLetter(String letter, int column) {
    		this.letter = letter;
    		this.column = column;
    	}
    	
    	public String getLetter() { return this.letter; }
    	public int getColumnIndex() { return this.column; }
    }
	
	@Override
	public String toString() {
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "problems representing me as a string";
		}
	}
}
