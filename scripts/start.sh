#!/bin/sh

echo "Starting application.."

nohup java -jar /root/apps/nfttradingmarket-0.0.1-SNAPSHOT.jar 2>&1  > /root/apps/logs/output.log &

echo "Started application."

