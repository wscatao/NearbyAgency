#!/bin/bash

aws dynamodb create-table \
    --table-name agency_registration \
    --attribute-definitions \
        AttributeName=agency_zipcode,AttributeType=S \
        AttributeName=agency_number,AttributeType=S \
        AttributeName=geohash_code,AttributeType=S \
    --key-schema \
        AttributeName=agency_zipcode,KeyType=HASH \
        AttributeName=agency_number,KeyType=RANGE \
    --global-secondary-indexes \
        "[
            {
                \"IndexName\": \"geohash_gsi\",
                \"KeySchema\": [
                    {\"AttributeName\": \"geohash_code\", \"KeyType\": \"HASH\"}
                ],
                \"Projection\": {
                    \"ProjectionType\": \"ALL\"
                }
            }
        ]" \
    --billing-mode PAY_PER_REQUEST \
    --endpoint-url http://localhost:4566
