This project helps the with the MP3 encoding at the No Regrets Men's Conference.  The day of the conference Steve Sonderman announces that all the seminars will be available by the end of the conference.  Do you think this just magically happens?  We (the volunteers) have written some automation that help us scrub the MP3s before you get them, which is what this project is all about.  For information on the conference visit the links below.

http://www.menwithnoregrets.org/
http://www.noregretsconference.org/
http://www.noregretsconference.org/audio/

Now for the technical stuff that this project depends on and how we use it.

Our high-level process is as follows:

1) MP3s are received from runners
2) Human upload to landingpad folder on server
3) App sleeps for 45 seconds
4) App automated the initial scrubbing (converts to mono, amplifies, removes beginning and ending silence, and copies the mp3 to the user edit folder)
5) MP3 team manually edit the mp3s from useredit folder on server and updates the spreadsheet as done ("d")
6) App automated the remainder of the MP3 tasks (adds the NR leadin, adds MP3 tags, puts in final folder, FTPs the mp3 to the NR server, and updates the spreadsheet as uploaded and ready for testing the link)
7) MP3 team test links and 2nd human review
8) Human marks spreadsheet as complete

To follow the above process, we need to setup our server correctly, so here we go (assuming you are running ubuntu server):

1) Setup Lame: http://wiki.audacityteam.org/wiki/Lame_Installation
   sudo apt-get install lame libmp3lame0
2) Setup SOX: http://superuser.com/questions/421153/how-to-add-a-mp3-handler-to-sox/421168
   sudo apt-get install sox
   sudo apt-get install libsox-fmt-mp3
3) Setup java 8: https://help.ubuntu.com/community/Java#Oracle_Java_8
   Method I used: http://www.webupd8.org/2012/09/install-oracle-java-8-in-ubuntu-via-ppa.html
   sudo add-apt-repository ppa:webupd8team/java
   sudo apt-get update
   sudo apt-get install oracle-java8-installer

To setup this app:

1) Install gradle: http://askubuntu.com/questions/328178/gradle-in-ubuntu
   sudo add-apt-repository ppa:cwchien/gradle
   sudo apt-get update
   sudo apt-get install gradle
2) Install git: http://askubuntu.com/questions/568591/how-do-i-install-the-latest-version-of-git-with-apt/568596
   sudo apt-add-repository ppa:git-core/ppa
   sudo apt-get update
   sudo apt-get install git
3) Pull down the code and configure: https://git-scm.com/book/en/v2/Git-Basics-Getting-a-Git-Repository
   git clone https://github.com/mattmatuszak/no-regrets-mp3-processing.git
4) Setup your user info (lot to talk about here...but not now)
   copy to the src/main/resources/userInfo.json
5) Create the directories required (need to decide where to run this app from)
   mkdir landingpad
   mkdir creds
   mkdir scripts
   mkdir preuseredit
   mkdir useredit
   mkdir postuseredit
   mkdir logs
   mkdir data
   mkdir final
   mkdir lib
6) Setup the properties file in src/main/resources/nr.properties
   Point to the directories created above
   Update the number of threads to the number of cores - 1 
7) Copy the Google Java files to the lib directory
   http://mvnrepository.com/artifact/com.google.gdata/core/1.47.1
   NOTE: Should look into this as opposed to using the old jars: https://github.com/google/google-api-java-client (the above seems to be required - at least for this code)
   NOTE: https://developers.google.com/google-apps/spreadsheets/
8) Run the gradle build
   gradle compileJava assemble
9) Copy to your lib directory (from the build/libs directory)
10)Copy leadin and image to data directory (from src/test/data)
11)Copy the scripts to the scripts directory (from src/main/scripts)
12)Run the app at the command line:
   java -jar ec-no-regrets-app-0.1.0.jar


*** Enhancements for 2017 ***
1) 48 ... check for 48 sample rate, and if not downgrade it
2) Cleanup if we revert...first step to check for pre and post user edit files, and if found delete the files
3) Have the app check for the dependent directories, and if not found, create the directories rather than having this info in the properties file
4) fix the profiles...for test, integration test, and live
5) Add alert or notification when landingpad or spreadsheet watcher terminates and then automatically restart
6) Update code to use the sox stat command to judge the quality of the audio before and after
