#!/bin/bash

docker rm mm-nr-mp3-processing

docker run \
-e "SPRING_PROFILES_ACTIVE=live" \
-v /home/matt/dev/temp/nr/data:/temp/nr/data \
-v /home/matt/dev/temp/nr/editor:/temp/nr/editor \
-v /home/matt/dev/temp/nr/final:/temp/nr/final \
-v /home/matt/dev/temp/nr/final_archive:/temp/nr/final_archive \
-v /home/matt/dev/temp/nr/logs:/temp/nr/logs \
-v /home/matt/dev/temp/nr/oauth2:/temp/nr/oauth2 \
-v /home/matt/dev/temp/nr/post:/temp/nr/post-edit \
-v /home/matt/dev/temp/nr/pre:/temp/nr/pre-edit \
-v /home/matt/dev/temp/nr/scripts:/temp/nr/scripts \
-v /home/matt/dev/temp/nr/upload:/temp/nr/upload \
-v /home/matt/dev/temp/nr/upload_archive:/temp/nr/upload_archive \
-v /home/matt/dev/temp/nr/userInfo:/temp/nr/userInfo \
-v /home/matt/dev/temp/nr/config:/temp/nr/config \
--name mm-nr-mp3-processing \
-p 8080:8080 -t mattmatuszak/nr-mp3-processing:latest

exit 0
