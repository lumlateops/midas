#!/bin/bash
# This script should be included at the top of BASH scripts to prevent multiple instances of the script
# e.g. write at the top of the script
#       source ./pid.sh

# Store PID of script:
# Match script without arguments
LCK_FILE=`basename $0`.lck

if [ -f "${LCK_FILE}" ]; then

  # The file exists so read the PID
  # to see if it is still running
  MYPID=`head -n 1 $LCK_FILE`

  if [ -n "`ps -p ${MYPID} | grep ${MYPID}`" ]; then
    echo `basename $0` is already running [$MYPID].
    exit
  fi
fi

# Echo current PID into lock file
echo $$ > $LCK_FILE
