#!/bin/bash

echo ""

while getopts ":s:t:" opt; do
  case $opt in
    s)
      sourceAudio=$OPTARG
      ;;
    t)
      targetAudio=$OPTARG
      ;;
    \?)
      echo "Invalid option:" $OPTARG
      ;;
  esac
done

echo "Source Audio:" $sourceAudio
echo "Target Audio:" $targetAudio

echo ""
echo "amplifying..."
sox -v `sox $sourceAudio -n stat -v 2>&1` $sourceAudio $targetAudio
echo "Finished amplifying."
echo ""

exit 0
