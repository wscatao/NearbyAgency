#!/bin/bash

aws dynamodb create-table \
    --table-name agency_registration \
    --attribute-definitions \
        AttributeName=bank_code,AttributeType=S \
        AttributeName=agency_number,AttributeType=S \
        AttributeName=geohash_code,AttributeType=S \
    --key-schema \
        AttributeName=bank_code,KeyType=HASH \
        AttributeName=agency_number,KeyType=RANGE \
    --local-secondary-indexes \
        "[
            {
                \"IndexName\": \"geohash_lsi\",
                \"KeySchema\": [
                    {\"AttributeName\": \"bank_code\", \"KeyType\": \"HASH\"},
                    {\"AttributeName\": \"geohash_code\", \"KeyType\": \"RANGE\"}
                ],
                \"Projection\": {
                    \"ProjectionType\": \"ALL\"
                }
            }
        ]" \
    --provisioned-throughput \
        ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --region sa-east-1
