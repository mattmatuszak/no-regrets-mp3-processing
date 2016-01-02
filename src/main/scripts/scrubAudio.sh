#!/bin/bash

echo ""

while getopts ":s:w:v:" opt; do
  case $opt in
    s)
      sourceAudio=$OPTARG
      ;;
    w)
      workingAudio=$OPTARG
      ;;
    v)
      waveAudio=$OPTARG
      ;;
    \?)
      echo "Invalid option:" $OPTARG 
      ;;
  esac
done

echo "Source Audio :" $sourceAudio
echo "Working Audio:" $workingAudio
echo "Wave Audio   :" $waveAudio

echo ""
echo "going down to MONO and reducing to a 48 bit rate..."
lame --mp3input --silent -m m -b 48 $sourceAudio $workingAudio
echo "Finished going down to MONO and reducing to a 48 buffer rate."
echo ""

echo "decoding to a WAV file..."
lame --decode --silent $workingAudio $waveAudio
echo "Finished decoding to a WAV file."

exit 0
