#!/bin/bash

aws dynamodb create-table \
    --table-name cadastro_agencia \
    --attribute-definitions \
        AttributeName=cep_agencia,AttributeType=S \
        AttributeName=numero_agencia,AttributeType=S \
        AttributeName=codigo_geohash,AttributeType=S \
    --key-schema \
        AttributeName=cep_agencia,KeyType=HASH \
        AttributeName=numero_agencia,KeyType=RANGE \
    --global-secondary-indexes \
        "[
            {
                \"IndexName\": \"geohash_gsi\",
                \"KeySchema\": [
                    {\"AttributeName\": \"codigo_geohash\", \"KeyType\": \"HASH\"},
                    {\"AttributeName\": \"numero_agencia\", \"KeyType\": \"RANGE\"}
                ],
                \"Projection\": {
                    \"ProjectionType\": \"ALL\"
                }
            }
        ]" \
    --billing-mode PAY_PER_REQUEST \
    --endpoint-url http://localhost:4566
