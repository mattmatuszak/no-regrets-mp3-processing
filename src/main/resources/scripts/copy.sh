#!/bin/bash

echo ""

while getopts ":s:t:l:" opt; do
  case $opt in
    s)
      sourceAudio=$OPTARG
      ;;
    t)
      finalAudio=$OPTARG
      ;;
    \?)
      echo "Invalid option:" $OPTARG
      ;;
  esac
done

echo "Source Audio:" $sourceAudio
echo "Target Audio:" $finalAudio

echo ""
echo "Copying..."
cp $sourceAudio "$finalAudio"
echo "Finished Copying."
echo ""

exit 0
