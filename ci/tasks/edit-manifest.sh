#!/bin/bash

set -xe

cat ./current-app-info/current-app.txt

sed "s/APPLICATION_NAME/$(cat ./current-app-info/next-app.txt)-$app_preffix/" ./demo-onlinestore-ui/manifest.yml > ./app-manifest-output/manifest.yml

cat ./app-manifest-output/manifest.yml