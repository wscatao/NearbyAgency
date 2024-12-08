#!/bin/bash

aws dynamodb delete-table \
    --table-name cadastro_agencia \
    --endpoint-url http://localhost:4566