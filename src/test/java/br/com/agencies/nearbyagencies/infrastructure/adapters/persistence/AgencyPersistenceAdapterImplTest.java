package br.com.agencies.nearbyagencies.infrastructure.adapters.persistence;

import br.com.agencies.nearbyagencies.domain.Agency;
import br.com.agencies.nearbyagencies.factory.AgencyFactory;
import br.com.agencies.nearbyagencies.infrastructure.adapters.exception.OptimisticLockingException;
import br.com.agencies.nearbyagencies.infrastructure.model.AgencyModel;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
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
    void findByBankCodeAndAgencyNumber_agency_found() {
        String bankCode = "341"; // Código do banco
        String agencyNumber = "2783"; // Número da agência

        AgencyModel agencyModel = new AgencyModel();
        agencyModel.setBankCode(bankCode);
        agencyModel.setAgencyNumber(agencyNumber);
        agencyModel.setAgencyZipCode("09111410");
        agencyModel.setAgencyName("Sto André Av. São Paulo");
        agencyModel.setAgencyTelephone("114004-4828");
        agencyModel.setAgencyEmail("2783@itau.com.br");
        agencyModel.setAgencyAddress("Av. São Paulo, 250 - Cidade São Jorge, Santo André - SP, 09111-410");
        agencyModel.setFormattedAddress("Formatted Address");
        agencyModel.setGeoHash("geoHash");
        agencyModel.setLatitude(10.0);
        agencyModel.setLongitude(20.0);

        when(dynamoDbTemplate.load(any(Key.class), eq(AgencyModel.class))).thenReturn(agencyModel);

        Optional<Agency> result = agencyPersistenceAdapter.findByBankCodeAndAgencyNumber(bankCode, agencyNumber);

        assertTrue(result.isPresent());
        assertEquals(bankCode, result.get().getBank().getCode());
        assertEquals(agencyNumber, result.get().getAgencyNumber());
    }

    @Test
    void findByBankCodeAndAgencyNumber_agency_not_found() {
        String zipCode = "09111410";
        String agencyNumber = "2783";

        when(dynamoDbTemplate.load(any(Key.class), eq(AgencyModel.class))).thenReturn(null);

        Optional<Agency> result = agencyPersistenceAdapter.findByBankCodeAndAgencyNumber(zipCode, agencyNumber);

        assertTrue(result.isEmpty());
    }

    @Test
    void update_agency_success() {
        Agency agency = AgencyFactory.createDefaultAgency();
        AgencyModel agencyModel = new AgencyModel();
        agencyModel.setAgencyZipCode(agency.getAgencyZipCode());
        agencyModel.setAgencyNumber(agency.getAgencyNumber());
        agencyModel.setVersion(agency.getVersion());

        when(dynamoDbTemplate.load(any(Key.class), eq(AgencyModel.class))).thenReturn(agencyModel);

        agencyPersistenceAdapter.update(agency);

        verify(dynamoDbTemplate, times(1)).update(any(AgencyModel.class));
    }

    @Test
    void update_agency_version_mismatch() {
        Agency agency = AgencyFactory.createDefaultAgency();
        AgencyModel agencyModel = new AgencyModel();
        agencyModel.setAgencyZipCode(agency.getAgencyZipCode());
        agencyModel.setAgencyNumber(agency.getAgencyNumber());
        agencyModel.setVersion(agency.getVersion());

        when(dynamoDbTemplate.load(any(Key.class), eq(AgencyModel.class))).thenReturn(agencyModel);
        doThrow(ConditionalCheckFailedException.class).when(dynamoDbTemplate).update(any(AgencyModel.class));

        assertThrows(OptimisticLockingException.class, () -> agencyPersistenceAdapter.update(agency));
    }

    @Test
    void delete_agency_success() {
        Agency agency = AgencyFactory.createDefaultAgency();
        AgencyModel agencyModel = new AgencyModel();
        agencyModel.setAgencyZipCode(agency.getAgencyZipCode());
        agencyModel.setAgencyNumber(agency.getAgencyNumber());

        when(dynamoDbTemplate.load(any(Key.class), eq(AgencyModel.class))).thenReturn(agencyModel);

        agencyPersistenceAdapter.delete(agency);

        verify(dynamoDbTemplate, times(1)).delete(any(AgencyModel.class));
    }

    @Test
    void delete_agency_version_mismatch() {
        Agency agency = AgencyFactory.createDefaultAgency();
        AgencyModel agencyModel = new AgencyModel();
        agencyModel.setAgencyZipCode(agency.getAgencyZipCode());
        agencyModel.setAgencyNumber(agency.getAgencyNumber());

        when(dynamoDbTemplate.load(any(Key.class), eq(AgencyModel.class))).thenReturn(agencyModel);
        doThrow(ConditionalCheckFailedException.class).when(dynamoDbTemplate).delete(any(AgencyModel.class));

        assertThrows(OptimisticLockingException.class, () -> agencyPersistenceAdapter.delete(agency));
    }

    @Test
    void delete_agency_not_found() {
        Agency agency = AgencyFactory.createDefaultAgency();
        when(dynamoDbTemplate.load(any(Key.class), eq(AgencyModel.class))).thenReturn(null);

        agencyPersistenceAdapter.delete(agency);

        verify(dynamoDbTemplate, times(0)).delete(any(AgencyModel.class));
    }
}
