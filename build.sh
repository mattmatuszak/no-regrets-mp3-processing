#!/bin/bash

echo ""

gradle build

if [ $? -eq 0 ]; then
    echo "Build OK"
else
    echo "Build Failed!!" $?
    exit 1
fi


docker build -t mattmatuszak/nr-mp3-processing:latest -t  mattmatuszak/nr-mp3-processing:1.0.1 .

exit 0