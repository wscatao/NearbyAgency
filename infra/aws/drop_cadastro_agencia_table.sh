#!/bin/bash

aws dynamodb delete-table \
    --table-name agency_registration \
    --region sa-east-1