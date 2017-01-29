FROM openjdk:8-jre-alpine

RUN apt-get update && apt-get install -y \
  lame \
  libmp3lame0 \
  sox \
  libsox-fmt-mp3
  
VOLUME /tmp/nr/landingpad
VOLUME /tmp/nr/landingpad-archive
VOLUME /tmp/nr/scripts
VOLUME /tmp/nr/logs
VOLUME /tmp/nr/data
VOLUME /tmp/nr/final
VOLUME /tmp/nr/pre-edit
VOLUME /tmp/nr/edit
VOLUME /tmp/nr/post-edit
VOLUME /tmp/nr/spreadsheet/oauth2
VOLUME /tmp/nr/userInfo

ADD ec-no-regrets-app-2017.1.0.jar ec-no-regrets-app.jar

RUN sh -c 'touch /ec-no-regrets-app.jar'

ENV JAVA_OPTS=""

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /ec-no-regrets-app.jar" ]
