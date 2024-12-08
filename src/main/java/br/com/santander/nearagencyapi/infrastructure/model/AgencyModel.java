package br.com.santander.nearagencyapi.infrastructure.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.List;

@DynamoDbBean
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class AgencyModel {

    private String agencyZipCode;
    private String agencyNumber;
    private String agencyName;
    private String agencyTelephone;
    private String agencyEmail;
    private String agencyAddress;
    private List<String> services;
    private String formattedAddress;
    private String geoHash;
    private double latitude;
    private double longitude;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("agency_zipcode")
    public String getAgencyZipCode() {
        return agencyZipCode;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("agency_number")
    public String getAgencyNumber() {
        return agencyNumber;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = {"geohash_gsi"})
    @DynamoDbAttribute("geohash_code")
    public String getGeoHash() {
        return geoHash;
    }

    public String getAgencyTelephone() {
        return agencyTelephone;
    }

    public String getAgencyEmail() {
        return agencyEmail;
    }

    public String getAgencyAddress() {
        return agencyAddress;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public List<String> getServices() {
        return services;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }
}
