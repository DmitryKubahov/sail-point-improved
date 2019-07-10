#!/bin/bash

ATTEMPTS=0

echo "Waiting for mysql"
until mysql -u $MYSQL_USER -h db -e "select 1 from $MYSQL_DATABASE.spt_identity" -p$MYSQL_PASSWORD &> /dev/null
do
  printf "\nWaiting for MySQL"
  printf "\nAttempts: $ATTEMPTS"
  sleep 1
  ATTEMPTS=$((ATTEMPTS+1))
done

printf "\nMySQL is running\n"
printf "\nTry to start tomcat\n"
catalina.sh jpda run

