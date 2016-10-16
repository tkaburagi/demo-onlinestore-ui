#!/bin/bash

set -xe

pwd
env

cf login -a $cf_api -u $cf_username -p $cf_password -o "$cf_organization" -s "$cf_space" --skip-ssl-validation

cf apps

set +e

cf apps | grep prd-$app_preffix | grep green

if [ $? -eq 0 ]
then
  echo "green" > ./current-app-info/current-app.txt
  echo "blue" > ./current-app-info/next-app.txt
else
  echo "blue" > ./current-app-info/current-app.txt
  echo "green" > ./current-app-info/next-app.txt
fi
set -xe

echo "Current main app routes to app instance $(cat ./current-app-info/current-app.txt)"
echo "New version of app to be deployed to instance $(cat ./current-app-info/next-app.txt)"