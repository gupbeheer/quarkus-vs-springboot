#!/bin/sh
#
# Run script to be packaged inside docker container for starting the service
#

JVM_OPTIONS="$*"

# jarfile is expected in the same directory as the run scripts
DIRNAME=`dirname $0`

CMD="java $JVM_OPTIONS -Dspring.profiles.active=k8s -jar $DIRNAME/service.jar"
echo "[INFO] Run: $CMD"
exec $CMD
