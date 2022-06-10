#!/bin/sh
#
# Run script to be packaged inside docker container for starting the service
#

JVM_OPTIONS="$*"

echo "[INFO] Run: java $JVM_OPTIONS -jar /deployments/quarkus-run.jar"
exec java $JVM_OPTIONS -jar /deployments/quarkus-run.jar
