#!/bin/bash

docker run \
-e "SPRING_PROFILES_ACTIVE=live" \
-v /tmp/docker/landingpad:/tmp/nr/landingpad \
-v  /tmp/docker/landingpad-archive:/tmp/nr/landingpad-archive \
-v  /tmp/docker/scripts:/tmp/nr/scripts \
-v  /tmp/docker/logs:/tmp/nr/logs \
-v  /tmp/docker/data:/tmp/nr/data \
-v  /tmp/docker/final:/tmp/nr/final \
-v  /tmp/docker/pre-edit:/tmp/nr/pre-edit \
-v  /tmp/docker/edit:/tmp/nr/edit \
-v  /tmp/docker/post-edit:/tmp/nr/post-edit \
-v  /tmp/docker/upload:/tmp/nr/upload \
-v  /tmp/nr/spreadsheet/oauth2:/tmp/nr/spreadsheet/oauth2 \
-v  /home/matt/dev:/tmp/nr/userInfo \
-p 50:8080 -p 55555:55555 -i -t no-regrets-mp3-processing:1.0

exit 0
