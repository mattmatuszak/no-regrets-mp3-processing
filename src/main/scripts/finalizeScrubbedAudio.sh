#!/bin/bash

echo ""

while getopts ":t:a:r:v:i:f:" opt; do
  case $opt in
    t)
      audioTitle=$OPTARG
      ;;
    a)
      audioAlbum=$OPTARG
      ;;
    r)
      audioArtist=$OPTARG
      ;;
    v)
      audioWav=$OPTARG
      ;;
    i)
      audioImage=$OPTARG
      ;;
    f)
      audioFinal=$OPTARG
      ;;
    \?)
      echo "Invalid option:" $OPTARG 
      ;;
  esac
done

echo "Audio Title : $audioTitle"
echo "Audio Album : $audioAlbum"
echo "Audio Artist: $audioArtist"
echo "Audio Wav   : $audioWav"
echo "Audio Image : $audioImage"
echo "Audio Final : $audioFinal"

echo ""
echo "encoding mp3 with all our tags..."
lame --mp3input --silent --tt "$audioTitle" --tl "$audioAlbum" --ta "$audioArtist" --add-id3v2 --ti $audioImage $audioWav $audioFinal
echo "Finished encoding mp3 with all our tags..."
echo ""


exit 0
