#!/bin/bash
set -e

API_URL="http://localhost:8080/api/deals"  # Change if needed

echo "Testing successful deal post (duplicates)..."
curl -s -X POST "$API_URL" -H "Content-Type: application/json" -d '[
  {
    "dealUniqueId": "DEAL001",
    "fromCurrencyIsoCode": "USD",
    "toCurrencyIsoCode": "EUR",
    "dealTimestamp": "2025-08-04T01:00:00Z",
    "dealAmountInOrderingCurrency": 1000.50
  },
  {
    "dealUniqueId":"3333",
    "fromCurrencyIsoCode": "GBP",
    "toCurrencyIsoCode": "USD",
    "dealTimestamp": "2025-08-04T01:00:00Z",
    "dealAmountInOrderingCurrency": 1500.00
  }
]' | jq

echo -e "\nTesting duplicate deals response..."
echo '{
    "message": "All deals were duplicates and skipped. | Success: 0 | Failed: 0 | Duplicates: 2",
    "failedDeals": [],
    "duplicateDeals": [
        "DEAL001",
        "3333"
    ],
    "successfulDeals": []
}' | jq

echo -e "\nTesting validation error response..."
curl -s -X POST "$API_URL" -H "Content-Type: application/json" -d '[
  {
    "dealUniqueId": "DEAL001",
    "fromCurrencyIsoCode": "USD",
    "toCurrencyIsoCode": "EUR",
    "dealTimestamp": "2025-08-04T01:00:00Z",
    "dealAmountInOrderingCurrency": -2
  },
  {
    "dealUniqueId":"",
    "fromCurrencyIsoCode": "GBP",
    "toCurrencyIsoCode": "USD",
    "dealTimestamp": "2025-08-04T01:00:00Z",
    "dealAmountInOrderingCurrency": 1500.00
  }
]' | jq

echo -e "\nTesting validation error JSON sample..."
echo '{
    "timestamp": "2025-08-04T02:46:39.843015523Z",
    "errors": {
        "0": [
            {
                "message": "Deal Amount must be greater than zero",
                "property": "dealAmountInOrderingCurrency"
            }
        ],
        "1": [
            {
                "message": "Deal Unique Id is required",
                "property": "dealUniqueId"
            }
        ]
    },
    "status": 400,
    "type": "Validation Error"
}' | jq
