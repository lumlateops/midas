#!/bin/bash
ROOT=/root/midas/current
cd $ROOT/var/run/$1
source $ROOT/bin/pid.sh
source $ROOT/env.sh
$JAVA_HOME/bin/java -cp $LIB/midas-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.lumlate.midas.email.InboxReader $ETC/midas.properties
