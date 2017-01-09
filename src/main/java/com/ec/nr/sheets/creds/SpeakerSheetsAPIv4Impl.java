package com.ec.nr.sheets.creds;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.ec.nr.sheets.creds.SpreadsheetDataRow.Field;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

public class SpeakerSheetsAPIv4Impl implements MP3SpreadsheetService {

	@Value( "${speaker.spreadsheet.appname}" )  private String applicationName;
	@Value( "${speaker.spreadsheet.userInfo}" )	private String userInfo;
	@Value( "${speaker.spreadsheet.id}" )	private String spreadsheetId;
	@Value( "${speaker.spreadsheet.sheet.name}" )	private String sheetName;
	@Value( "${speaker.spreadsheet.sheet.range.key-status}" )	private String keyStatusRange;

	@Autowired private java.io.File credentialDirectory;
	
    private FileDataStoreFactory dataStoreFactory;
    private HttpTransport httpTransport;
    private Sheets sheetService;

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS);

    public SpeakerSheetsAPIv4Impl() {  super();  }
    
    @PostConstruct
	public void initialize() {
    	try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(credentialDirectory);
            sheetService = getSheetsService();
            
        } catch (Throwable t) {
        	System.err.println("problems accessing the spreadsheet...see trace below!!");
            t.printStackTrace();
        }
    }
    
    private Sheets getSheetsService() throws IOException {
        Credential credential = authorize();
        return new Sheets.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(applicationName)
                .build();
    }
    
    private Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
            SpeakerSheetsAPIv4Impl.class.getResourceAsStream(userInfo);
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(dataStoreFactory)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
            flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + credentialDirectory.getAbsolutePath());
        return credential;
    }
	
	
	
	
	@Override
	public Map<String, String> getAllMP3Statuses() {
		
		HashMap<String, String> mp3Statuses = new HashMap<String, String>();
		
		try {
			ValueRange response = sheetService.spreadsheets().values()
				.get(spreadsheetId, sheetName + "!" + keyStatusRange).execute();
			
			List<List<Object>> values = response.getValues();
			System.out.println(response.getMajorDimension());
			if (values == null || values.size() == 0) {
				System.out.println("No data found.");
			} else {
				for (List row : values) {
					mp3Statuses.put(String.valueOf(row.get(0)), String.valueOf(row.get(1)));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return mp3Statuses;
	}
	@Override
	public void updateBreakoutStatus(String mp3NameToUpdate, String state) {
		
	}

	@Override
	public void updateField(String id, Field field, String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public SpreadsheetDataRow getAudioDetails(String mp3ToRetrieve) {
		// TODO Auto-generated method stub
		return null;
	}

}
