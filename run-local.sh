#!/bin/bash

export GOOGLE_APPLICATION_CREDENTIALS="./service-account.json"

./gradlew bootRun
