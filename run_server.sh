#!/bin/bash

git pull origin master

sudo ./gradlew clean build

sudo docker stop gramo-backend
sudo docker rm gramo-backend

sudo docker-compose up --build -d  
