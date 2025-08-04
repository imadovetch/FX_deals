#!/bin/bash

# FX Deals API Test Script
# This script tests the FX Deals API with sample data

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

# Check if curl is available
check_curl() {
    if ! command -v curl &> /dev/null; then
        print_error "curl is not installed. Please install curl first."
        exit 1
    fi
    print_success "curl is available"
}

# Check if the application is running
check_application() {
    print_status "Checking if the application is running..."
    
    if curl -f http://localhost:8080/actuator/health &> /dev/null; then
        print_success "Application is running"
        return 0
    else
        print_error "Application is not running. Please start it first using: ./scripts/deploy.sh"
        exit 1
    fi
}

# Test with empty array
test_empty_array() {
    print_status "Testing with empty array..."
    
    response=$(curl -s -X POST http://localhost:8080/api/deals/import \
        -H "Content-Type: application/json" \
        -d '[]')
    
    echo "Response: $response"
    
    if echo "$response" | grep -q "Success: 0"; then
        print_success "Empty array test passed"
    else
        print_warning "Empty array test may have failed"
    fi
}

# Test with single valid deal
test_single_deal() {
    print_status "Testing with single valid deal..."
    
    response=$(curl -s -X POST http://localhost:8080/api/deals/import \
        -H "Content-Type: application/json" \
        -d '[
            {
                "dealUniqueId": "TEST001",
                "fromCurrencyIsoCode": "USD",
                "toCurrencyIsoCode": "EUR",
                "dealTimestamp": 1640995200000,
                "dealAmountInOrderingCurrency": 1000.00
            }
        ]')
    
    echo "Response: $response"
    
    if echo "$response" | grep -q "Success: 1"; then
        print_success "Single deal test passed"
    else
        print_warning "Single deal test may have failed"
    fi
}

# Test with invalid data
test_invalid_data() {
    print_status "Testing with invalid data..."
    
    response=$(curl -s -X POST http://localhost:8080/api/deals/import \
        -H "Content-Type: application/json" \
        -d '[
            {
                "dealUniqueId": "TEST002",
                "fromCurrencyIsoCode": "INVALID",
                "toCurrencyIsoCode": "EUR",
                "dealTimestamp": 1640995200000,
                "dealAmountInOrderingCurrency": 1000.00
            }
        ]')
    
    echo "Response: $response"
    
    if echo "$response" | grep -q "400"; then
        print_success "Invalid data test passed (correctly rejected)"
    else
        print_warning "Invalid data test may have failed"
    fi
}

# Test with duplicate deals
test_duplicate_deals() {
    print_status "Testing with duplicate deals..."
    
    # First, add a deal
    curl -s -X POST http://localhost:8080/api/deals/import \
        -H "Content-Type: application/json" \
        -d '[
            {
                "dealUniqueId": "TEST003",
                "fromCurrencyIsoCode": "USD",
                "toCurrencyIsoCode": "EUR",
                "dealTimestamp": 1640995200000,
                "dealAmountInOrderingCurrency": 1000.00
            }
        ]' > /dev/null
    
    # Then try to add the same deal again
    response=$(curl -s -X POST http://localhost:8080/api/deals/import \
        -H "Content-Type: application/json" \
        -d '[
            {
                "dealUniqueId": "TEST003",
                "fromCurrencyIsoCode": "USD",
                "toCurrencyIsoCode": "EUR",
                "dealTimestamp": 1640995200000,
                "dealAmountInOrderingCurrency": 1000.00
            }
        ]')
    
    echo "Response: $response"
    
    if echo "$response" | grep -q "Duplicates: 1"; then
        print_success "Duplicate deals test passed"
    else
        print_warning "Duplicate deals test may have failed"
    fi
}

# Test with sample data file
test_sample_data() {
    print_status "Testing with sample data file..."
    
    if [ -f "sample-data/deals-sample.json" ]; then
        response=$(curl -s -X POST http://localhost:8080/api/deals/import \
            -H "Content-Type: application/json" \
            -d @sample-data/deals-sample.json)
        
        echo "Response: $response"
        
        if echo "$response" | grep -q "Success:"; then
            print_success "Sample data test passed"
        else
            print_warning "Sample data test may have failed"
        fi
    else
        print_warning "Sample data file not found: sample-data/deals-sample.json"
    fi
}

# Test with large dataset
test_large_dataset() {
    print_status "Testing with large dataset..."
    
    # Create a large dataset
    large_data="["
    for i in {1..100}; do
        if [ $i -gt 1 ]; then
            large_data="$large_data,"
        fi
        large_data="$large_data{
            \"dealUniqueId\": \"BULK$i\",
            \"fromCurrencyIsoCode\": \"USD\",
            \"toCurrencyIsoCode\": \"EUR\",
            \"dealTimestamp\": $((1640995200000 + i * 1000)),
            \"dealAmountInOrderingCurrency\": $((1000 + i))
        }"
    done
    large_data="$large_data]"
    
    response=$(curl -s -X POST http://localhost:8080/api/deals/import \
        -H "Content-Type: application/json" \
        -d "$large_data")
    
    echo "Response: $response"
    
    if echo "$response" | grep -q "Success:"; then
        print_success "Large dataset test passed"
    else
        print_warning "Large dataset test may have failed"
    fi
}

# Main test function
main() {
    print_status "Starting FX Deals API tests..."
    
    check_curl
    check_application
    
    echo ""
    print_status "Running API tests..."
    echo ""
    
    test_empty_array
    echo ""
    
    test_single_deal
    echo ""
    
    test_invalid_data
    echo ""
    
    test_duplicate_deals
    echo ""
    
    test_sample_data
    echo ""
    
    test_large_dataset
    echo ""
    
    print_success "All API tests completed!"
    print_status "Check the responses above for detailed results."
}

# Handle script arguments
case "${1:-all}" in
    "all")
        main
        ;;
    "empty")
        check_curl
        check_application
        test_empty_array
        ;;
    "single")
        check_curl
        check_application
        test_single_deal
        ;;
    "invalid")
        check_curl
        check_application
        test_invalid_data
        ;;
    "duplicate")
        check_curl
        check_application
        test_duplicate_deals
        ;;
    "sample")
        check_curl
        check_application
        test_sample_data
        ;;
    "large")
        check_curl
        check_application
        test_large_dataset
        ;;
    *)
        echo "Usage: $0 {all|empty|single|invalid|duplicate|sample|large}"
        echo "  all      - Run all tests (default)"
        echo "  empty    - Test with empty array"
        echo "  single   - Test with single deal"
        echo "  invalid  - Test with invalid data"
        echo "  duplicate- Test with duplicate deals"
        echo "  sample   - Test with sample data file"
        echo "  large    - Test with large dataset"
        exit 1
        ;;
esac 