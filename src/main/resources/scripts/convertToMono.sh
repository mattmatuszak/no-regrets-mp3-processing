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
echo "going down to MONO and reducing to a 48 bit rate..."
lame --mp3input --silent -m m -b 64 $sourceAudio $targetAudio
echo "Finished going down to MONO and reducing to a 64 buffer rate."
echo ""

exit 0
