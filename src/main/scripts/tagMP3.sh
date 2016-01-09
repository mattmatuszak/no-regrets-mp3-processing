#!/bin/bash

echo ""

while getopts ":s:t:e:l:r:i:" opt; do
  case $opt in
    s)
      sourceAudio=$OPTARG
      ;;
    t)
      targetAudio=$OPTARG
      ;;
    e)
      audioTitle=$OPTARG
      ;;
    l)
      audioAlbum=$OPTARG
      ;;
    r)
      audioArtist=$OPTARG
      ;;
    i)
      audioImage=$OPTARG
      ;;
    \?)
      echo "Invalid option:" $OPTARG
      ;;
  esac
done

echo "Source Audio:" $sourceAudio
echo "Target Audio:" $targetAudio
echo "Audio Title :" $audioTitle
echo "Audio Album :" $audioAlbum
echo "Audio Artist:" $audioArtist
echo "Audio Image :" $audioImage

echo ""
echo "Taging MP3..."
lame --mp3input --silent --tt "$audioTitle" --tl "$audioAlbum" --ta "$audioArtist" --add-id3v2 --ti $audioImage $sourceAudio $targetAudio
echo "Finished Taging MP3."
echo ""

exit 0
