package com.ec.nr.sheets.creds;

import java.util.HashMap;
import java.util.List;


public class SpreadsheetDataRow {
	
	private HashMap<Field, String> attributes;
	
	protected SpreadsheetDataRow(List<Object> headers, List<Object> attributes) {
		this.attributes = new HashMap<>();
		
		for (int headerIndex = 0; headerIndex < headers.size(); headerIndex++) {
			
			Field headerField = Field.fromUserFriendlyName(String.valueOf(headers.get(headerIndex)));
			
			if (headerField != null) {
				if (attributes.size() <= headerIndex) {
					this.attributes.put(headerField, String.valueOf(attributes.get(headerIndex)));
				}
			}
			
		}
	}
	
	public enum Field { 		
		  SPEAKER_FIRST_NAME("firstname", "First Name")
		, SPEAKER_LAST_NAME("lastname", "Last Name")
		, SPEAKER(null, "Speaker")
		, SEMINAR("seminartitle", "Seminar Title")
		, CONFERENCE("conference", "Conference")
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
			
			if (friendlyId != null) {
			
				for (Field field : Field.values()) {
					if (field.userFriendlyName.equals(friendlyId)) {
						return field;
					}
				}
			}
			
			return null;
			
		}
		
		public String getUserFriendlyName() {
			return this.userFriendlyName;
		}
		
		public String getFieldName() {
			return this.fieldName;
		}
		
	}
	
	public String getFieldValue(Field field) {
		
		switch (field) {
		case SPEAKER:
			return this.attributes.get(Field.SPEAKER_FIRST_NAME) + " " + this.attributes.get(Field.SPEAKER_LAST_NAME);
		default:
			return this.attributes.get(field);
		}
	}
	
	
}
