package com.ec.nr.sheets.creds;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;

import com.google.gdata.data.spreadsheet.CustomElementCollection;

public class SpreadsheetDataRow {
	
	@Value( "${mp3.album.title}" )
	String conferenceName;
	
	private HashMap<Field, String> attributes;
	
	protected SpreadsheetDataRow(CustomElementCollection attributes) {
		this.attributes = new HashMap<>();
		
		this.attributes.put(Field.MP3_ID, attributes.getValue(Field.MP3_ID.getFieldName()));
		this.attributes.put(Field.MP3_STATE, attributes.getValue(Field.MP3_STATE.getFieldName()));
		this.attributes.put(Field.SEMINAR, attributes.getValue(Field.SEMINAR.getFieldName()));
		this.attributes.put(Field.SPEAKER_FIRST_NAME, attributes.getValue(Field.SPEAKER_FIRST_NAME.getFieldName()));
		this.attributes.put(Field.SPEAKER_LAST_NAME, attributes.getValue(Field.SPEAKER_LAST_NAME.getFieldName()));
	}

	public enum Field { 		
		  SPEAKER_FIRST_NAME("firstname")
		, SPEAKER_LAST_NAME("lastname")
		, SPEAKER(null)
		, SEMINAR("seminartitle")
		, CONFERENCE(null)
		, MP3_ID("mp3name")
		, MP3_STATE("mp3State")
		;
		private String fieldName;
		
		private Field(String fieldName) {
			this.fieldName = fieldName;
		}
		
		public String getFieldName() {
			return this.fieldName;
		}
	}
	
	public String getFieldValue(Field field) {
		
		switch (field) {
		case SPEAKER:
			return this.attributes.get(Field.SPEAKER_FIRST_NAME) + " " + this.attributes.get(Field.SPEAKER_LAST_NAME);
		case CONFERENCE:
			return conferenceName;
		default:
			return this.attributes.get(field);
		}
	}
	
	
}
