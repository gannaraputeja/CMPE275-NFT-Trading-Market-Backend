#!/bin/sh

cd /root/CMPE275-NFT-Trading-Market-Backend/

git pull origin main
git status

./gradlew clean build

cp -f /root/CMPE275-NFT-Trading-Market-Backend/build/libs/nfttradingmarket-0.0.1-SNAPSHOT.jar /root/apps/



