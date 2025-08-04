.PHONY: build up test-api down test

all: build up test-api

# Build the Docker image
build:
	docker build -t fx-deals-app .

# Start services using docker-compose
up:
	docker-compose up -d

# Run API tests
test-api:
	@echo "Running API tests..."
	@./scripts/test-api.sh
	# just for testing apis

# Run unit tests (during build)
test: build

# Stop and clean up containers
down:
	docker-compose down