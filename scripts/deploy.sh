#!/bin/bash
set -e

echo "Building Docker image..."
docker build -t my-app .

echo "Starting Docker Compose services..."
docker-compose up -d

echo "Waiting for services to start..."
sleep 5

echo "Deployment done."
