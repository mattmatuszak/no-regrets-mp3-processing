package com.ec.nr.sheets.creds;

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
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Tokeninfo;
import com.google.api.services.oauth2.model.Userinfoplus;
import com.google.gdata.client.spreadsheet.SpreadsheetQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CustomElementCollection;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
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

@Service
public class SpeakerSpreadsheet {

	private static Logger logger = LogManager.getLogger(SpeakerSpreadsheet.class);

	@Value( "${speaker.spreadsheet.appname}" )
	private String applicationName;
	@Value( "${speaker.spreadsheet.userInfo}" )
	private String userInfo;
	@Value( "${speaker.spreadsheet.name}" )
	private String spreadsheetName;
	@Value( "${speaker.spreadsheet.sheet}" )
	private String sheetName;

	@Autowired private java.io.File credentialDirectory;
	
	private FileDataStoreFactory dataStoreFactory;
	private HttpTransport httpTransport;
	private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private final List<String> SCOPES = Arrays.asList(
			"https://www.googleapis.com/auth/userinfo.profile",
			"https://www.googleapis.com/auth/userinfo.email",
			"https://spreadsheets.google.com/feeds");

	private Oauth2 oauth2;
	private Credential credential;
	private GoogleClientSecrets clientSecrets;

	/** Authorizes the installed application to access user's protected data. */
	private Credential authorize() throws Exception {
		// load client secrets
		clientSecrets = GoogleClientSecrets.load(
				JSON_FACTORY,
				new InputStreamReader(SpeakerSpreadsheet.class
						.getResourceAsStream(userInfo)));

		if (clientSecrets.getDetails().getClientId().startsWith("Enter")
				|| clientSecrets.getDetails().getClientSecret()
						.startsWith("Enter ")) {

			logger.error("BIG PROBLEM...we cannot obtain our client info to login!");
		}
		// set up authorization code flow
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
				.setDataStoreFactory(dataStoreFactory)
				.setApprovalPrompt("force").build();
		// authorize
		return new AuthorizationCodeInstalledApp(flow,
				new LocalServerReceiver()).authorize("user");
	}

	@PostConstruct
	public void initialize() {
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			dataStoreFactory = new FileDataStoreFactory(credentialDirectory);
			// authorization
			credential = authorize();
			// set up global Oauth2 instance
			oauth2 = new Oauth2.Builder(httpTransport, JSON_FACTORY, credential)
					.setApplicationName(applicationName).build();
			// run commands
			tokenInfo(credential.getAccessToken());
			userInfo();
			spreadsheetInfo(credential);
			// success!
			return;
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		System.exit(1);
	}

	private void tokenInfo(String accessToken) throws IOException {
		header("Validating a token");
		Tokeninfo tokeninfo = oauth2.tokeninfo().setAccessToken(accessToken)
				.execute();
		logger.debug(tokeninfo.toPrettyString());
		if (!tokeninfo.getAudience().equals(
				clientSecrets.getDetails().getClientId())) {
			logger.error("ERROR: audience does not match our client ID!");
		}
	}

	private void spreadsheetInfo(Credential cred) throws IOException,
			ServiceException {
		SpreadsheetService service = new SpreadsheetService(
				"MySpreadsheetIntegration-v1");
		service.setOAuth2Credentials(cred);

		URL SPREADSHEET_FEED_URL = new URL(
				"https://spreadsheets.google.com/feeds/spreadsheets/private/full");

		SpreadsheetQuery query = new SpreadsheetQuery(SPREADSHEET_FEED_URL);
		query.setTitleQuery(spreadsheetName);
		query.setTitleExact(true);
		// Make a request to the API and get all spreadsheets.
		// SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL,
		// SpreadsheetFeed.class);
		SpreadsheetFeed feed = service.getFeed(query, SpreadsheetFeed.class);
		List<SpreadsheetEntry> spreadsheets = feed.getEntries();

		// Iterate through all of the spreadsheets returned
		for (SpreadsheetEntry spreadsheet : spreadsheets) {
			// Print the title of this spreadsheet to the screen
			logger.debug(spreadsheet.getTitle().getPlainText());
		}

	}

	private void userInfo() throws IOException {
		header("Obtaining User Profile Information");
		Userinfoplus userinfo = oauth2.userinfo().get().execute();
		logger.debug(userinfo.toPrettyString());
	}

	private void header(String name) {
		logger.trace("");
		logger.trace("================== " + name + " ==================");
		logger.trace("");
	}
	
	public Map<String, String> getAllMP3Statuses() {
		
		HashMap<String, String> mp3Statuses = new HashMap<String, String>();
		
		try {
			
			SpreadsheetService service = new SpreadsheetService(
					"MySpreadsheetIntegration-v1");
			service.setOAuth2Credentials(credential);
	
			URL SPREADSHEET_FEED_URL = new URL(
					"https://spreadsheets.google.com/feeds/spreadsheets/private/full");
	
			SpreadsheetQuery query = new SpreadsheetQuery(SPREADSHEET_FEED_URL);
			query.setTitleQuery(spreadsheetName);
			query.setTitleExact(true);
			// Make a request to the API and get all spreadsheets.
			// SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL,
			// SpreadsheetFeed.class);
			SpreadsheetFeed feed = service.getFeed(query, SpreadsheetFeed.class);
			List<SpreadsheetEntry> spreadsheets = feed.getEntries();
	
			// Iterate through all of the spreadsheets returned
			for (SpreadsheetEntry spreadsheet : spreadsheets) {
				
				
				List<WorksheetEntry> sheets = spreadsheet.getWorksheets();
	
				for (WorksheetEntry sheet : sheets) {
	
					if (!sheet.getTitle().getPlainText().equalsIgnoreCase(this.sheetName)) {
						logger.trace("ignoring sheet '" + sheet.getTitle().getPlainText() + "'...we only care about the " + sheetName);
					} else {
						logger.trace("Looking at Sheet Name:" + sheet.getTitle().getPlainText());
		
						ListFeed rows = service.getFeed(sheet.getListFeedUrl(), ListFeed.class);
		
						for (ListEntry row : rows.getEntries()) {
							
							CustomElementCollection attributes = row.getCustomElements();
							
							String mp3Name = attributes.getValue("mp3name");
							String mp3State = attributes.getValue("mp3State");
							String title = attributes.getValue("seminartitle");
							String album = "No Regrets Conference 2014";
							String artist = attributes.getValue("firstname") + " " + attributes.getValue("lastname");
							//String bypassLeadin = attributes.getValue("bypassleadin");
							
							logger.trace("Evaluating seminar..." + mp3Name + ":" + mp3State);
							
							mp3Statuses.put(mp3Name, mp3State);
							
						}
					}
				}
				
				
				
			}
		
		} catch (Throwable t) {
			logger.error("problems updating the spreadsheet!", t);
			throw new Error("problems updating the spreadsheet!", t);
		}
		
		return mp3Statuses;
	}

	public void updateBreakoutStatus(String mp3NameToUpdate, String stage) {
		logger.debug("updating status on mp3 " + mp3NameToUpdate + " to " + stage + "...");

		try {
		
			SpreadsheetService service = new SpreadsheetService(
					"MySpreadsheetIntegration-v1");
			service.setOAuth2Credentials(credential);
	
			URL SPREADSHEET_FEED_URL = new URL(
					"https://spreadsheets.google.com/feeds/spreadsheets/private/full");
	
			SpreadsheetQuery query = new SpreadsheetQuery(SPREADSHEET_FEED_URL);
			query.setTitleQuery(spreadsheetName);
			query.setTitleExact(true);
			// Make a request to the API and get all spreadsheets.
			// SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL,
			// SpreadsheetFeed.class);
			SpreadsheetFeed feed = service.getFeed(query, SpreadsheetFeed.class);
			List<SpreadsheetEntry> spreadsheets = feed.getEntries();
	
			// Iterate through all of the spreadsheets returned
			for (SpreadsheetEntry spreadsheet : spreadsheets) {
				
				
				List<WorksheetEntry> sheets = spreadsheet.getWorksheets();
	
				for (WorksheetEntry sheet : sheets) {
	
					if (!sheet.getTitle().getPlainText().equalsIgnoreCase(sheetName)) {
						logger.trace("ignoring sheet '" + sheet.getTitle().getPlainText() + "'...we only care about the Breakout");
					} else {
						logger.trace("Looking at Sheet Name:" + sheet.getTitle().getPlainText());
		
						ListFeed rows = service.getFeed(sheet.getListFeedUrl(), ListFeed.class);
		
						boolean rowUpdated = false;
		
						for (ListEntry row : rows.getEntries()) {
							
							CustomElementCollection attributes = row.getCustomElements();
							
							String mp3Name = attributes.getValue("mp3name");
							String mp3State = attributes.getValue("mp3State");
							String title = attributes.getValue("seminartitle");
							String album = "No Regrets Conference 2014";
							String artist = attributes.getValue("firstname") + " " + attributes.getValue("lastname");
							//String bypassLeadin = attributes.getValue("bypassleadin");
							
							logger.trace("Evaluating seminar..." + mp3Name + ":" + mp3State);
							
							if (mp3Name != null && mp3NameToUpdate != null && mp3NameToUpdate.equalsIgnoreCase(mp3Name)
									&& mp3State != null) {
									//&& (mp3State == null || mp3State.equalsIgnoreCase(""))) {
								logger.debug("updating row to ready!");
								row.getCustomElements().setValueLocal("mp3State", stage);
								row.update();
							}
							
						}
					}
				}
				
				
				
			}
		
		} catch (Throwable t) {
			logger.error("problems updating the spreadsheet!", t);
			throw new Error("problems updating the spreadsheet!", t);
		}
		
		
		
		
		
		logger.trace("finished updating status on mp3 " + mp3NameToUpdate + " to " + stage + ".");
	}
	
	
	public void updateField(String id, SpreadsheetDataRow.Field field, String value) {
		logger.trace("updating " + id + " --> " + field.getFieldName() + " : " + value + "...");

		try {
		
			SpreadsheetService service = new SpreadsheetService(
					"MySpreadsheetIntegration-v1");
			service.setOAuth2Credentials(credential);
	
			URL SPREADSHEET_FEED_URL = new URL(
					"https://spreadsheets.google.com/feeds/spreadsheets/private/full");
	
			SpreadsheetQuery query = new SpreadsheetQuery(SPREADSHEET_FEED_URL);
			query.setTitleQuery(spreadsheetName);
			query.setTitleExact(true);
			// Make a request to the API and get all spreadsheets.
			// SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL,
			// SpreadsheetFeed.class);
			SpreadsheetFeed feed = service.getFeed(query, SpreadsheetFeed.class);
			List<SpreadsheetEntry> spreadsheets = feed.getEntries();
	
			// Iterate through all of the spreadsheets returned
			for (SpreadsheetEntry spreadsheet : spreadsheets) {
				
				
				List<WorksheetEntry> sheets = spreadsheet.getWorksheets();
	
				for (WorksheetEntry sheet : sheets) {
	
					if (!sheet.getTitle().getPlainText().equalsIgnoreCase(sheetName)) {
						logger.trace("ignoring sheet '" + sheet.getTitle().getPlainText() + "'...we only care about the Breakout");
					} else {
						logger.trace("Looking at Sheet Name:" + sheet.getTitle().getPlainText());
		
						ListFeed rows = service.getFeed(sheet.getListFeedUrl(), ListFeed.class);
		
						boolean rowUpdated = false;
		
						for (ListEntry row : rows.getEntries()) {
							
							CustomElementCollection attributes = row.getCustomElements();
							
							String mp3Name = attributes.getValue(SpreadsheetDataRow.Field.MP3_ID.getFieldName());
														
							if (mp3Name != null && id != null && id.equalsIgnoreCase(mp3Name)) {
									//&& (mp3State == null || mp3State.equalsIgnoreCase(""))) {
								row.getCustomElements().setValueLocal(field.getFieldName(), value);
								row.update();
							}
							
						}
					}
				}
				
			}
		
		} catch (Throwable t) {
			logger.error("problems updating the spreadsheet!", t);
			throw new Error("problems updating the spreadsheet!", t);
		}
		
		
		
		
		
		logger.trace("finished updating " + id + " --> " + field + " : " + value + "...");
	}
	
	public SpreadsheetDataRow getAudioDetails(String mp3ToRetrieve) {
		logger.trace("getting audio details for " + mp3ToRetrieve + "...");
		SpreadsheetDataRow data = null;

		try {
		
			SpreadsheetService service = new SpreadsheetService(
					"MySpreadsheetIntegration-v1");
			service.setOAuth2Credentials(credential);
	
			URL SPREADSHEET_FEED_URL = new URL(
					"https://spreadsheets.google.com/feeds/spreadsheets/private/full");
	
			SpreadsheetQuery query = new SpreadsheetQuery(SPREADSHEET_FEED_URL);
			query.setTitleQuery(spreadsheetName);
			query.setTitleExact(true);
			// Make a request to the API and get all spreadsheets.
			// SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL,
			// SpreadsheetFeed.class);
			SpreadsheetFeed feed = service.getFeed(query, SpreadsheetFeed.class);
			List<SpreadsheetEntry> spreadsheets = feed.getEntries();
	
			// Iterate through all of the spreadsheets returned
			for (SpreadsheetEntry spreadsheet : spreadsheets) {
				
				
				List<WorksheetEntry> sheets = spreadsheet.getWorksheets();
	
				for (WorksheetEntry sheet : sheets) {
	
					if (!sheet.getTitle().getPlainText().equalsIgnoreCase(sheetName)) {
						logger.trace("ignoring sheet '" + sheet.getTitle().getPlainText() + "'...we only care about the Breakout");
					} else {
						logger.trace("Looking at Sheet Name:" + sheet.getTitle().getPlainText());
		
						ListFeed rows = service.getFeed(sheet.getListFeedUrl(), ListFeed.class);
		
						boolean rowUpdated = false;
		
						for (ListEntry row : rows.getEntries()) {
							
							CustomElementCollection attributes = row.getCustomElements();
							
							String mp3Name = attributes.getValue("mp3name");
													
							if (mp3Name != null && mp3ToRetrieve != null && mp3ToRetrieve.equalsIgnoreCase(mp3Name)) {
								data = new SpreadsheetDataRow(attributes);
							}
							
						}
					}
				}
			}
		
		} catch (Throwable t) {
			logger.error("problems updating the spreadsheet!", t);
			throw new Error("problems updating the spreadsheet!", t);
		}
		
		logger.trace("finished getting audio details for " + mp3ToRetrieve + ".");
		
		return data;
	}
	

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public void setCredentialDirectory(java.io.File credentialDirectory) {
		this.credentialDirectory = credentialDirectory;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}
}
