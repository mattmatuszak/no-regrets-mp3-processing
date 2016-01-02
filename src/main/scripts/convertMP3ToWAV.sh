#!/bin/bash

echo ""

while getopts ":m:w:" opt; do
  case $opt in
    m)
      mp3Audio=$OPTARG
      ;;
    w)
      wavAudio=$OPTARG
      ;;
    \?)
      echo "Invalid option:" $OPTARG 
      ;;
  esac
done

echo "MP3 :" $mp3Audio
echo "WAV :" $wavAudio

#echo ""
#echo "going down to MONO and reducing to a 48 bit rate..."
#lame --mp3input --silent -m m -b 48 $sourceAudio $workingAudio
#echo "Finished going down to MONO and reducing to a 48 buffer rate."
#echo ""

echo "decoding to a WAV file..."
lame --decode --silent $mp3Audio $wavAudio
echo "Finished decoding to a WAV file."

exit 0
