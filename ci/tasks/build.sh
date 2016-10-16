#!/bin/sh
cd demo-onlinestore-ui
./mvnw clean package -DskipTests=true
mv target/*.war ../build/demo-onlinestore-ui.jar