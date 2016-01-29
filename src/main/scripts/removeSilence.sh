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
echo "removing silence..."
sox $sourceAudio $targetAudio silence 1 0.1 0.5% reverse silence 1 0.1 0.5% reverse
echo "Finished removing silence."
echo ""

exit 0
