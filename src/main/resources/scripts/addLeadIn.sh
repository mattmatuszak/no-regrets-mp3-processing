#!/bin/bash

echo ""

while getopts ":s:t:l:" opt; do
  case $opt in
    s)
      sourceAudio=$OPTARG
      ;;
    t)
      targetAudio=$OPTARG
      ;;
    l)
      leadIn=$OPTARG
      ;;
    \?)
      echo "Invalid option:" $OPTARG
      ;;
  esac
done

echo "Source Audio:" $sourceAudio
echo "Target Audio:" $targetAudio
echo "Leadin Audio:" $leadIn

echo ""
echo "Adding Lead into Audio..."
sox $leadIn $sourceAudio $targetAudio
echo "Finished Adding Lead into Audio."
echo ""

exit 0
