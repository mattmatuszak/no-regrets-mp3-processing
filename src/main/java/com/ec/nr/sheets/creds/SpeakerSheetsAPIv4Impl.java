package com.ec.nr.sheets.creds;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ec.nr.NREnvironment;
import com.ec.nr.sheets.creds.SpreadsheetDataRow.Field;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

@Service
public class SpeakerSheetsAPIv4Impl implements MP3SpreadsheetService, SheetConnectionSerivce {

	private static final Logger logger = LogManager.getLogger(SpeakerSheetsAPIv4Impl.class);
	
	@Value( "${speaker.spreadsheet.appname}" )  private String applicationName;
	@Value( "${speaker.spreadsheet.id}" )	private String spreadsheetId;
	@Value( "${speaker.spreadsheet.sheet.name}" )	private String sheetName;
	@Value( "${speaker.spreadsheet.sheet.range.key-status}" )	private String keyStatusRange;
	@Value( "${speaker.spreadsheet.sheet.range.all}" )	private String allRange;

	@Autowired private NREnvironment nrEnvironment;
	
    private FileDataStoreFactory dataStoreFactory;
    private HttpTransport httpTransport;
    private Sheets sheetService;
    
    private SpreadsheetRange allSpreadsheetRange;

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS);

    public SpeakerSheetsAPIv4Impl() {  super();  }
    
    @PostConstruct
	public void initialize() {
    	try {
    		
    		logger.info("appname:{}", applicationName);
    		logger.info("id:{}", spreadsheetId);
    		logger.info("sheet.name:{}", sheetName);
    		logger.info("sheet.range.key-status:{}", keyStatusRange);
    		logger.info("sheet.range.all:{}", allRange);
    		
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(new File(nrEnvironment.SPREADSHEET_OAUTH2_DIR));
            
            sheetService = getSheetsService();
            mapFieldToColumnLetter();
            
        } catch (Throwable t) {
        	System.err.println("problems accessing the spreadsheet...see trace below!!");
            t.printStackTrace();
        }
    }
    
    

	private void mapFieldToColumnLetter() throws IOException {
		ValueRange response = sheetService.spreadsheets().values()
				.get(spreadsheetId, sheetName + "!" + injectRowRange(allRange, 1)).execute();
		List<List<Object>> rows = response.getValues();
		if (rows == null || rows.size() <= 0) {
			System.out.println("No data found.");
		} else {
			Map<Field, SpreadsheetRange.ColumnIndexLetter> rangeMapping = new HashMap<>();
			for (int columnIndex = 0; columnIndex < rows.get(0).size(); columnIndex++) {
				Field column = SpreadsheetDataRow.Field.fromUserFriendlyName(String.valueOf(rows.get(0).get(columnIndex)));
				if (column == null) {
					logger.info("IGNORING field:".concat(String.valueOf(rows.get(0).get(columnIndex))));
				} else { 
					rangeMapping.put(column, new SpreadsheetRange.ColumnIndexLetter(String.valueOf((char) (65+columnIndex)), columnIndex));
				}
			}
			
			this.allSpreadsheetRange = new SpreadsheetRange(allRange, rangeMapping);
		}
		
		logger.info("letter to column {}", this.allSpreadsheetRange);
	}
    
    private String injectRowRange(String baseRange, int row) {
    	if (baseRange != null) {
    		if (baseRange.indexOf(":") >= 0) {
    			String[] splitRange = baseRange.split(":");
    			String injectedRange = splitRange[0].concat(String.valueOf(row)).concat(":").concat(splitRange[1]).concat(String.valueOf(row));
    			logger.trace("injected range:{}", injectedRange);
    			return injectedRange;
    		}
    		else {
    			String injectedRange = baseRange.concat(String.valueOf(row));
    			logger.trace("injected range:{}", injectedRange);
    			return injectedRange;
    		}
    	}
    	
    	return baseRange;
    }
    
    private Sheets getSheetsService() throws IOException {
        Credential credential = authorizeViaConsole();
        return new Sheets.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(applicationName)
                .build();
    }
    
    
    private Credential authorizeViaSomethingElse(String authCode) throws IOException {
    	// Load client secrets.
        InputStream in = new FileInputStream(nrEnvironment.SPREADSHEET_OAUTH2_USER_INFO_DIR + "/" + nrEnvironment.SPREADSHEET_OAUTH2_USER_NAME);
            //SpeakerSheetsAPIv4Impl.class.getResourceAsStream(nrEnvironment.SPREADSHEET_OAUTH2_USER_INFO);
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        
    	GoogleTokenResponse tokenResponse =
    	          new GoogleAuthorizationCodeTokenRequest(
    	              new NetHttpTransport(),
    	              JacksonFactory.getDefaultInstance(),
    	              "https://www.googleapis.com/oauth2/v4/token",
    	              clientSecrets.getDetails().getClientId(),
    	              clientSecrets.getDetails().getClientSecret(),
    	              authCode,
    	              "")  // Specify the same redirect URI that you use with your web
    	                             // app. If you don't have a web version of your app, you can
    	                             // specify an empty string.
    	              .execute();

    	String accessToken = tokenResponse.getAccessToken();

    	// Use access token to call API
    	GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
    	return credential;
    }
    
    
    private Credential authorizeViaConsole() throws IOException {
        // Load client secrets.
        InputStream in = new FileInputStream(nrEnvironment.SPREADSHEET_OAUTH2_USER_INFO_DIR + "/" + nrEnvironment.SPREADSHEET_OAUTH2_USER_NAME);
            //SpeakerSheetsAPIv4Impl.class.getResourceAsStream(nrEnvironment.SPREADSHEET_OAUTH2_USER_INFO);
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
                "Credentials saved to " + nrEnvironment.SPREADSHEET_OAUTH2_DIR);
        return credential;
    }
	
	private int getRowForMP3(String id) {
		try {
			ValueRange response = sheetService.spreadsheets().values()
				.get(spreadsheetId, sheetName + "!" + keyStatusRange).execute();
			List<List<Object>> values = response.getValues();
			if (values == null || values.size() == 0) {
				System.out.println("No data found.");
			} else {
				for (int rowIndex = 1; rowIndex < values.size(); rowIndex++) {
					if (values.get(rowIndex).get(0) != null && values.get(rowIndex).get(0).equals(id))
						return (rowIndex + 1);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	
	@Override
	public Map<String, String> getAllMP3Statuses() {
		
		HashMap<String, String> mp3Statuses = new HashMap<String, String>();
		
		try {
			ValueRange response = sheetService.spreadsheets().values()
				.get(spreadsheetId, sheetName + "!" + keyStatusRange).execute();
			List<List<Object>> values = response.getValues();
			if (values == null || values.size() == 0) {
				System.out.println("No data found.");
			} else {
				for (List row : values) {
					mp3Statuses.put(String.valueOf(row.get(0)), (row.size() >= 2) ? String.valueOf(row.get(1)) : null);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return mp3Statuses;
	}

	@Override
	public void updateField(String id, Field field, String value) {
		
		try {
			String rangeToUpdate = injectRowRange(this.allSpreadsheetRange.getFieldToColumnLetterMap().get(field).getLetter(), getRowForMP3(id));
			
			ValueRange newValue = new ValueRange();
			List<List<Object>> list  = new ArrayList<>();
			List<Object> values = new ArrayList<>();
			values.add(value);
			list.add(values);
			newValue.setValues(list);
			newValue.setRange(rangeToUpdate);
			
			logger.trace("new value: {}", newValue);
			logger.trace("range to update: {}", rangeToUpdate);
			
			BatchUpdateValuesRequest updateRequest = new BatchUpdateValuesRequest();
			updateRequest.setValueInputOption("USER_ENTERED");
			updateRequest.setData(Arrays.asList(newValue));
			
			BatchUpdateValuesResponse response = sheetService.spreadsheets().values()
				.batchUpdate
				(
						spreadsheetId
						, updateRequest
				).execute();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private ValueRange getData(String range) throws IOException {
		logger.trace("range->{}", range);
		return sheetService.spreadsheets().values()
				.get(spreadsheetId, sheetName + "!" + range).execute();
	}

	@Override
	public SpreadsheetDataRow getAudioDetails(String mp3ToRetrieve) {
		logger.trace("mp3 details to retrieve {}", mp3ToRetrieve);
		try {
			
			ValueRange response = getData(injectRowRange(allRange, getRowForMP3(mp3ToRetrieve)));
			
			List<List<Object>> values = response.getValues(); 
			if (values == null || values.size() <= 0) {
				System.out.println("No data found.");
			} else {
				return new SpreadsheetDataRow(allSpreadsheetRange, values.get(0));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean connect() throws Exception {
		sheetService = getSheetsService();
		mapFieldToColumnLetter();
		return true;
	}

}
