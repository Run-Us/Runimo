#!/bin/bash

SERVICE_NAME="Runimo"
pid=`ps -elf | grep org.runimo.$SERVICE_NAME.jar`

if [ -z "$pid"]; then
  echo "$SERVICE_NAME killed"
else
  kill -9 $pid
  echo "$SERVICE_NAME $pid closing..." process exit
fi