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
		  SPEAKER_FIRST_NAME("firstname", "First Name")
		, SPEAKER_LAST_NAME("lastname", "Last Name")
		, SPEAKER(null, "Speaker")
		, SEMINAR("seminartitle", "Seminar Title")
		, CONFERENCE(null, "Conference")
		, MP3_ID("mp3name", "MP3 Id")
		, MP3_STATE("mp3State", "MP3 State")
		, CONVERT_TO_MONO("converttomono", "Convert To Mono")
		, AMPLIFY("amplify", "Amplify")
		, REMOVE_SILENCE("removesilence", "Remove Silence")
		, COPY_TO_USER_EDIT("copytouseredit", "Copy To User Edit")
		, ADD_LEAD_IN("addleadin", "Add Lead In")
		, TAG_MP3("tagmp3", "Tag MP3")
		, COPY_TO_FINAL("copytofinal", "Copy To Final")
		, FTP("ftp", "FTP")
		;
		private String fieldName;
		private String userFriendlyName;
		
		private Field(String fieldName, String userFriendlyName) {
			this.fieldName = fieldName;
			this.userFriendlyName = userFriendlyName;
		}
		
		public static Field fromUserFriendlyName(String friendlyId) {
			
			for (Field field : Field.values()) {
				if (field.userFriendlyName.equals(friendlyId)) {
					return field;
				}
			}
			
			return null;
			
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
