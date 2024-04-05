#!/bin/bash

./mvnw clean verify

if [ $? -eq 0 ]; then
  echo "Maven build successful. Building Docker containers..."
  docker compose build --no-cache
  if [ $? -eq 0 ]; then
    echo "Docker Compose build successful. Starting Docker containers..."
    docker compose up -d
    echo "Docker containers started successfully."
  else
    echo "Docker Compose build failed. Aborting."
  fi
else
  echo "Maven build failed. Aborting Docker container build and startup."
fi
