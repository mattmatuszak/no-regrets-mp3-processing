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
export SSHPASS=CHANGEME
sshpass -e sftp -oBatchMode=no -b - user@ip:port << !
   cd /usr/src
   put $sourceAudio
   bye
!
echo "Finished FTPing to server."
echo ""

exit 0
