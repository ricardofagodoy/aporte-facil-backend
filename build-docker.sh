#!/bin/bash

PROJECT_ID=balanceamento-carteira-333420
IMAGE_NAME=aporte-facil-backend
TEMP_CLOUD_FOLDER=build/cloud

# Create JAR
./gradlew build

# Create Cloud Build temp folder
mkdir $TEMP_CLOUD_FOLDER

# Copy needed files
cp build/libs/*.jar $TEMP_CLOUD_FOLDER
cp Dockerfile $TEMP_CLOUD_FOLDER

# Build to gcr.io using Cloud Build
cd $TEMP_CLOUD_FOLDER
gcloud builds submit --tag gcr.io/"$PROJECT_ID"/"$IMAGE_NAME"
