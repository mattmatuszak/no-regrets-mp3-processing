FROM ubuntu
RUN apt-get update && apt-get install -y \
  lame \
  libmp3lame0 \
  sox \
  libsox-fmt-mp3 \
  openjdk-8-jre

RUN mkdir /temp && mkdir /temp/nr

VOLUME /temp/nr/data
VOLUME /temp/nr/editor
VOLUME /temp/nr/final
VOLUME /temp/nr/final_archive
VOLUME /temp/nr/logs
VOLUME /temp/nr/oauth2
VOLUME /temp/nr/post-edit
VOLUME /temp/nr/pre-edit
VOLUME /temp/nr/scripts
VOLUME /temp/nr/upload
VOLUME /temp/nr/upload_archive
VOLUME /temp/nr/userInfo
VOLUME /temp/nr/config

ADD build/libs/ec-no-regrets-app-2017.1.0.jar ec-no-regrets-app.jar

RUN sh -c 'touch /ec-no-regrets-app.jar'

ENV JAVA_OPTS=""

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /ec-no-regrets-app.jar --spring.config.location=file:/temp/nr/config/application.properties" ]
