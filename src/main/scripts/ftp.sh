#!/bin/bash

echo ""

while getopts ":s:u:p:" opt; do
  case $opt in
    s)
      sourceAudio=$OPTARG
      ;;
    u)
      user=$OPTARG
      ;;
    p)
      pass=$OPTARG
      ;;
    \?)
      echo "Invalid option:" $OPTARG
      ;;
  esac
done

echo "Source Audio:" $sourceAudio
echo "User:" $user
echo "Pass:*********"

echo ""
echo "FTPing file to server..."
###cp $sourceAudio $finalAudio
echo "Finished FTPing to server."
echo ""

exit 0
