#!/bin/sh

echo "Stopping application.."

jps -l | grep /root/apps/nfttradingmarket-0.0.1-SNAPSHOT.jar | cut -d ' ' -f 1 | xargs -rn1 kill

mv /root/apps/logs/output.log /root/apps/logs/output_$(date +%F'-'%T).log

echo "Stopped application."

