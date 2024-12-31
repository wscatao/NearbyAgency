#!/bin/bash

aws dynamodb delete-table \
    --table-name agency_registration \
    --endpoint-url http://localhost:4566