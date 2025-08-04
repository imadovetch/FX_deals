#!/bin/bash

# FX Deals Application Deployment Script
# This script builds and deploys the FX Deals application using Docker Compose

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if Docker is installed
check_docker() {
    if ! command -v docker &> /dev/null; then
        print_error "Docker is not installed. Please install Docker first."
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        print_error "Docker Compose is not installed. Please install Docker Compose first."
        exit 1
    fi
    
    print_success "Docker and Docker Compose are available"
}

# Check if Maven is installed
check_maven() {
    if ! command -v mvn &> /dev/null; then
        print_warning "Maven is not installed. The build will be done inside Docker."
    else
        print_success "Maven is available"
    fi
}

# Build the application
build_application() {
    print_status "Building the application..."
    
    if command -v mvn &> /dev/null; then
        print_status "Building with Maven..."
        mvn clean package -DskipTests
        print_success "Application built successfully"
    else
        print_status "Maven not available, will build in Docker"
    fi
}

# Deploy the application
deploy_application() {
    print_status "Starting the application stack..."
    
    # Stop existing containers
    print_status "Stopping existing containers..."
    docker-compose down --remove-orphans
    
    # Build and start the application
    print_status "Building and starting containers..."
    docker-compose up --build -d
    
    # Wait for services to be ready
    print_status "Waiting for services to be ready..."
    sleep 30
    
    # Check if services are healthy
    check_services_health
}

# Check services health
check_services_health() {
    print_status "Checking services health..."
    
    # Check PostgreSQL
    if docker-compose ps postgres | grep -q "healthy"; then
        print_success "PostgreSQL is healthy"
    else
        print_error "PostgreSQL is not healthy"
        docker-compose logs postgres
        exit 1
    fi
    
    # Check application
    if docker-compose ps fx-deals-app | grep -q "healthy"; then
        print_success "FX Deals application is healthy"
    else
        print_warning "FX Deals application health check failed, checking logs..."
        docker-compose logs fx-deals-app
    fi
}

# Test the application
test_application() {
    print_status "Testing the application..."
    
    # Wait a bit more for the application to be fully ready
    sleep 10
    
    # Test the health endpoint
    if curl -f http://localhost:8080/actuator/health &> /dev/null; then
        print_success "Application health check passed"
    else
        print_warning "Application health check failed, but continuing..."
    fi
    
    # Test the API endpoint
    if curl -f http://localhost:8080/api/deals/import -X POST -H "Content-Type: application/json" -d '[]' &> /dev/null; then
        print_success "API endpoint is accessible"
    else
        print_warning "API endpoint test failed"
    fi
}

# Show application information
show_info() {
    print_status "Application Information:"
    echo "  - Application URL: http://localhost:8080"
    echo "  - API Endpoint: http://localhost:8080/api/deals/import"
    echo "  - PostgreSQL: localhost:5432"
    echo "  - pgAdmin (if enabled): http://localhost:5050"
    echo ""
    print_status "Sample data file: sample-data/deals-sample.json"
    print_status "To test the API, use:"
    echo "  curl -X POST http://localhost:8080/api/deals/import \\"
    echo "    -H 'Content-Type: application/json' \\"
    echo "    -d @sample-data/deals-sample.json"
}

# Main deployment function
main() {
    print_status "Starting FX Deals application deployment..."
    
    check_docker
    check_maven
    build_application
    deploy_application
    test_application
    show_info
    
    print_success "Deployment completed successfully!"
    print_status "You can now access the application at http://localhost:8080"
}

# Handle script arguments
case "${1:-deploy}" in
    "deploy")
        main
        ;;
    "stop")
        print_status "Stopping the application stack..."
        docker-compose down
        print_success "Application stopped"
        ;;
    "restart")
        print_status "Restarting the application stack..."
        docker-compose restart
        print_success "Application restarted"
        ;;
    "logs")
        print_status "Showing application logs..."
        docker-compose logs -f
        ;;
    "clean")
        print_status "Cleaning up Docker resources..."
        docker-compose down --volumes --remove-orphans
        docker system prune -f
        print_success "Cleanup completed"
        ;;
    *)
        echo "Usage: $0 {deploy|stop|restart|logs|clean}"
        echo "  deploy  - Build and deploy the application (default)"
        echo "  stop    - Stop the application"
        echo "  restart - Restart the application"
        echo "  logs    - Show application logs"
        echo "  clean   - Clean up Docker resources"
        exit 1
        ;;
esac 