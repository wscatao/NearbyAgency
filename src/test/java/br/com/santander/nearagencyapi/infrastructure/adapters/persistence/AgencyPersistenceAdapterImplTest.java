package br.com.santander.nearagencyapi.infrastructure.adapters.persistence;

import br.com.santander.nearagencyapi.domain.Agency;
import br.com.santander.nearagencyapi.factory.AgencyFactory;
import br.com.santander.nearagencyapi.infrastructure.model.AgencyModel;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AgencyPersistenceAdapterImplTest {

    @Mock
    private DynamoDbTemplate dynamoDbTemplate;

    @InjectMocks
    private AgencyPersistenceAdapterImpl agencyPersistenceAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_agency_success() {
        Agency agency = AgencyFactory.createDefaultAgency();

        doAnswer(invocation -> {
            AgencyModel agencyModel = invocation.getArgument(0);
            assertNotNull(agencyModel);
            return null;
        }).when(dynamoDbTemplate).save(any(AgencyModel.class));

        agencyPersistenceAdapter.save(agency);

        verify(dynamoDbTemplate, times(1)).save(any(AgencyModel.class));
    }

    @Test
    void findByCepAndNumber_agency_found() {
        String zipCode = "09111410";
        String agencyNumber = "2783";

        AgencyModel agencyModel = new AgencyModel();
        agencyModel.setAgencyZipCode(zipCode);
        agencyModel.setAgencyNumber(agencyNumber);
        agencyModel.setAgencyName("Sto André Av. São Paulo");
        agencyModel.setAgencyTelephone("114004-4828");
        agencyModel.setAgencyEmail("2783@itau.com.br");
        agencyModel.setAgencyAddress("Av. São Paulo, 250 - Cidade São Jorge, Santo André - SP, 09111-410");
        agencyModel.setFormattedAddress("Formatted Address");
        agencyModel.setGeoHash("geoHash");
        agencyModel.setLatitude(10.0);
        agencyModel.setLongitude(20.0);

        when(dynamoDbTemplate.load(any(Key.class), eq(AgencyModel.class))).thenReturn(agencyModel);

        Optional<Agency> result = agencyPersistenceAdapter.findByCepAndNumber(zipCode, agencyNumber);

        assertTrue(result.isPresent());
        assertEquals(zipCode, result.get().getAgencyZipCode());
        assertEquals(agencyNumber, result.get().getAgencyNumber());
    }

    @Test
    void findByCepAndNumber_agency_not_found() {
        String zipCode = "09111410";
        String agencyNumber = "2783";

        when(dynamoDbTemplate.load(any(Key.class), eq(AgencyModel.class))).thenReturn(null);

        Optional<Agency> result = agencyPersistenceAdapter.findByCepAndNumber(zipCode, agencyNumber);

        assertTrue(result.isEmpty());
    }

}
