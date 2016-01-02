#!/bin/bash

echo $AS_LANDING_PAD_DIR
echo $AS_LOGS_DIR
echo $AS_SCRIPTS_DIR
echo $AS_WORKING_DIR
echo $AS_DATA_DIR
echo $AS_SCRUBBED_DIR


java -cp "$AS_LIB_DIR/*" com.ec.nr.main.ProcessAudio $AS_LANDING_PAD_DIR $AS_LOGS_DIR $AS_SCRIPTS_DIR $AS_WORKING_DIR $AS_DATA_DIR $AS_SCRUBBED_DIR 2

javaExit=$?

echo "java exit code:" $javaExit

exit $javaExit
