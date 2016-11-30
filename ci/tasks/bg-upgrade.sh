#!/bin/bash

set -xe

pwd
env

cf login -a $cf_api -u $cf_username -p $cf_password -o "$cf_organization" -s "$cf_space" --skip-ssl-validation

cf apps

cf routes

export NEXT_APP_COLOR=$(cat ./current-app-info/next-app.txt)
export NEXT_APP_NAME=$NEXT_APP_COLOR-$app_preffix

export CURRENT_APP_COLOR=$(cat ./current-app-info/current-app.txt)
export CURRENT_APP_NAME=$CURRENT_APP_COLOR-$app_preffix

echo "Mapping main app route to point to $NEXT_APP_HOSTNAME instance"
cf map-route $NEXT_APP_NAME $app_domain --hostname prd-$app_preffix

cf routes

echo "Removing previous main app route that pointed to $CURRENT_APP_HOSTNAME instance"


sleep 30; #for demo

set +e
cf unmap-route $CURRENT_APP_NAME $app_domain --hostname prd-$app_preffix
cf unmap-route $CURRENT_APP_NAME $app_domain --hostname $app_preffix-temp

cf delete -f $CURRENT_APP_NAME
#cf unmap-route $NEXT_APP_NAME $app_domain --hostname $app_preffix-temp
set -e

echo "Routes updated"

cf routes