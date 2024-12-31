package br.com.agencies.nearbyagencies.infrastructure.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.extensions.annotations.DynamoDbVersionAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.List;

@DynamoDbBean
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class AgencyModel {

    private String bankCode;
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
    private Long version;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("bank_code")
    public String getBankCode() {return bankCode; }

    @DynamoDbSortKey
    @DynamoDbAttribute("agency_number")
    public String getAgencyNumber() {
        return agencyNumber;
    }
    public String getAgencyZipCode() { return agencyZipCode; }

    @DynamoDbSecondarySortKey(indexNames = {"geohash_lsi"})
    @DynamoDbAttribute("geohash_code")
    public String getGeoHash() {
        return geoHash;
    }

    @DynamoDbAttribute("agency_telephone")
    public String getAgencyTelephone() {
        return agencyTelephone;
    }

    @DynamoDbAttribute("agency_email")
    public String getAgencyEmail() {
        return agencyEmail;
    }

    @DynamoDbAttribute("agency_address")
    public String getAgencyAddress() {
        return agencyAddress;
    }

    @DynamoDbAttribute("agency_name")
    public String getAgencyName() {
        return agencyName;
    }

    @DynamoDbAttribute("latitude")
    public double getLatitude() {
        return latitude;
    }

    @DynamoDbAttribute("longitude")
    public double getLongitude() {
        return longitude;
    }

    @DynamoDbAttribute("services")
    public List<String> getServices() {
        return services;
    }

    @DynamoDbAttribute("formatted_address")
    public String getFormattedAddress() {
        return formattedAddress;
    }

    @DynamoDbVersionAttribute
    @DynamoDbAttribute("version")
    public Long getVersion() {
        return version;
    }
}
