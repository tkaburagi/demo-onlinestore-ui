#!/bin/sh
cd demo-onlinestore-ui
./mvnw clean package -DskipTests=true
mv target/*.jar ../build/demo-onlinestore-ui.jar