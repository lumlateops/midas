#!/bin/bash
ROOT=/root/midas/current
source $ROOT/bin/pid.sh
source $ROOT/env.sh
cd $MIDAS_HOME
$JAVA_HOME/bin/java -Xms128m -Xmx256m -cp $LIB/midas-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.lumlate.midas.email.InboxReader $ETC/midas.properties
