package br.com.agencies.nearbyagencies.interfaces;

import br.com.agencies.nearbyagencies.application.usecases.AgencyUseCases;
import br.com.agencies.nearbyagencies.domain.Agency;
import br.com.agencies.nearbyagencies.domain.exception.AgencyNotFoundException;
import br.com.agencies.nearbyagencies.factory.AgencyFactory;
import br.com.agencies.nearbyagencies.infrastructure.adapters.exception.OptimisticLockingException;
import br.com.agencies.nearbyagencies.infrastructure.util.Page;
import br.com.agencies.nearbyagencies.interfaces.dto.CreateAgencyDto;
import br.com.agencies.nearbyagencies.interfaces.dto.UpdateAgencyDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AgencyController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AgencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AgencyUseCases agencyUseCases;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class CreateAgencyTests {

        private CreateAgencyDto createAgencyDto;

        @Test
        void createAgency() throws Exception {
            Mockito.doNothing().when(agencyUseCases).createAgency(Mockito.any());

            mockMvc.perform(post("/agencies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(AgencyFactory.createDefaultAgencyDto())))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "http://localhost/agencies/341/2783"));
        }

        @Test
        void createAgencyWithInvalidData() throws Exception {
            createAgencyDto = AgencyFactory.createDefaultAgencyDto();
            createAgencyDto.setAgencyZipCode(null);

            mockMvc.perform(post("/agencies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createAgencyDto)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void createAgencyWithInvalidZipCode() throws Exception {
            createAgencyDto = AgencyFactory.createWithInvalidZipCode();

            mockMvc.perform(post("/agencies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createAgencyDto)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void createAgencyWithInvalidEmail() throws Exception {
            createAgencyDto = AgencyFactory.createWithInvalidEmail();

            mockMvc.perform(post("/agencies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createAgencyDto)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void createAgencyWithMissingFields() throws Exception {
            createAgencyDto = AgencyFactory.createWithMissingFields();

            mockMvc.perform(post("/agencies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createAgencyDto)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void createAgencyWithInvalidDataProblemDetail() throws Exception {
            createAgencyDto = AgencyFactory.createDefaultAgencyDto();
            createAgencyDto.setAgencyZipCode("091114100"); // Invalid zip code

            mockMvc.perform(post("/agencies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createAgencyDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.type").value("about:blank"))
                    .andExpect(jsonPath("$.title").value("Validation error"))
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.detail").value("One or more parameters are invalid"))
                    .andExpect(jsonPath("$.instance").value("/agencies"))
                    .andExpect(jsonPath("$.invalid-parameters[0].parameterName").value("agencyZipCode"))
                    .andExpect(jsonPath("$.invalid-parameters[0].message").value("zip code must have 8 digits"))
                    .andExpect(jsonPath("$.invalid-parameters[0].value").value("091114100"));
        }

        @Test
        void createAgencyWithInvalidZipCodeProblemDetail() throws Exception {
            createAgencyDto = AgencyFactory.createWithInvalidZipCode();

            mockMvc.perform(post("/agencies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createAgencyDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.type").value("about:blank"))
                    .andExpect(jsonPath("$.title").value("Validation error"))
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.detail").value("One or more parameters are invalid"))
                    .andExpect(jsonPath("$.instance").value("/agencies"))
                    .andExpect(jsonPath("$.invalid-parameters[0].parameterName").value("agencyZipCode"))
                    .andExpect(jsonPath("$.invalid-parameters[0].message").value("zip code must have 8 digits"))
                    .andExpect(jsonPath("$.invalid-parameters[0].value").value("123"));
        }

        @Test
        void createAgencyWithInvalidEmailProblemDetail() throws Exception {
            createAgencyDto = AgencyFactory.createWithInvalidEmail();

            mockMvc.perform(post("/agencies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createAgencyDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.type").value("about:blank"))
                    .andExpect(jsonPath("$.title").value("Validation error"))
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.detail").value("One or more parameters are invalid"))
                    .andExpect(jsonPath("$.instance").value("/agencies"))
                    .andExpect(jsonPath("$.invalid-parameters[0].parameterName").value("agencyEmail"))
                    .andExpect(jsonPath("$.invalid-parameters[0].message").value("Invalid email"))
                    .andExpect(jsonPath("$.invalid-parameters[0].value").value("invalid-email"));
        }

        @Test
        void createAgencyWithMissingFieldsProblemDetail() throws Exception {
            createAgencyDto = AgencyFactory.createWithMissingFields();

            mockMvc.perform(post("/agencies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createAgencyDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.type").value("about:blank"))
                    .andExpect(jsonPath("$.title").value("Validation error"))
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.detail").value("One or more parameters are invalid"))
                    .andExpect(jsonPath("$.instance").value("/agencies"));
        }
    }

    @Nested
    class GetAgencyByCepAndNumberTests {

        @Test
        void getAgencyByCepAndNumber() throws Exception {
            Agency agency = AgencyFactory.createDefaultAgency();
            Mockito.when(agencyUseCases.getAgency(Mockito.anyString(), Mockito.anyString()))
                    .thenReturn(agency);

            mockMvc.perform(get("/agencies/341/2783")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.agencyNumber").value("2783"))
                    .andExpect(jsonPath("$.agencyName").value(agency.getAgencyName()))
                    .andExpect(jsonPath("$.agencyTelephone").value(agency.getAgencyTelephone()))
                    .andExpect(jsonPath("$.agencyEmail").value(agency.getAgencyEmail()))
                    .andExpect(jsonPath("$.agencyAddress").value(agency.getAgencyAddress()));
        }

        @Test
        void getAgencyByCepAndNumberNotFound() throws Exception {
            Mockito.when(agencyUseCases.getAgency(Mockito.anyString(), Mockito.anyString()))
                    .thenThrow(new AgencyNotFoundException("Agency not found or does not exist"));

            mockMvc.perform(get("/agencies/341/2783")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        void getAgencyByCepAndNumberInvalidZipCode() throws Exception {
            mockMvc.perform(get("/agencies/invalid/2783")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void getAgencyByCepAndNumberInvalidAgencyNumber() throws Exception {
            mockMvc.perform(get("/agencies/09111410/invalid")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class UpdateAgencyTests {
        @Test
        void updateAgency() throws Exception {
            UpdateAgencyDto updateAgencyDto = AgencyFactory.createDefaultUpdateAgencyDto();
            Mockito.doNothing().when(agencyUseCases).updateAgency(Mockito.any());

            mockMvc.perform(patch("/agencies/341/2783")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("If-Match", "\"1\"")
                            .content(objectMapper.writeValueAsString(updateAgencyDto)))
                    .andExpect(status().isOk());
        }

        @Test
        void updateAgencyWithInvalidZipCode() throws Exception {
            UpdateAgencyDto updateAgencyDto = AgencyFactory.createDefaultUpdateAgencyDto();

            mockMvc.perform(patch("/agencies/invalid/2783")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("If-Match", "\"1\"")
                            .content(objectMapper.writeValueAsString(updateAgencyDto)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void updateAgencyWithInvalidAgencyNumber() throws Exception {
            UpdateAgencyDto updateAgencyDto = AgencyFactory.createDefaultUpdateAgencyDto();

            mockMvc.perform(patch("/agencies/341/invalid")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("If-Match", "\"1\"")
                            .content(objectMapper.writeValueAsString(updateAgencyDto)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void updateAgencyWithMissingIfMatchHeader() throws Exception {
            UpdateAgencyDto updateAgencyDto = AgencyFactory.createDefaultUpdateAgencyDto();

            mockMvc.perform(patch("/agencies/341/2783")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateAgencyDto)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void updateAgencyWithVersionMismatch() throws Exception {
            UpdateAgencyDto updateAgencyDto = AgencyFactory.createDefaultUpdateAgencyDto();
            Mockito.doThrow(new OptimisticLockingException("Version mismatch - update failed"))
                    .when(agencyUseCases).updateAgency(Mockito.any());

            mockMvc.perform(patch("/agencies/341/2783")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("If-Match", "\"1\"")
                            .content(objectMapper.writeValueAsString(updateAgencyDto)))
                    .andExpect(status().isPreconditionFailed());
        }
    }

    @Nested
    class DeleteAgency {
        @Test
        void deleteAgency_success() throws Exception {
            doNothing().when(agencyUseCases).deleteAgency(any());

            mockMvc.perform(delete("/agencies/341/2783")
                            .header("If-Match", "\"1\"")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        void deleteAgency_notFound() throws Exception {
            doThrow(new AgencyNotFoundException("Agency not found")).when(agencyUseCases).deleteAgency(any());

            mockMvc.perform(delete("/agencies/341/2783")
                            .header("If-Match", "\"1\"")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        void deleteAgency_versionMismatch() throws Exception {
            doThrow(new OptimisticLockingException("Version mismatch - delete failed")).when(agencyUseCases).deleteAgency(any());

            mockMvc.perform(delete("/agencies/341/2783")
                            .header("If-Match", "\"wrong-version\"")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isPreconditionFailed());
        }
    }

    @Nested
    class GetNearbyAgenciesTests {

        @Test
        void getNearbyAgencies_success() throws Exception {
            Page<Agency> page = new Page<>(Collections.emptyList(), null);
            Mockito.when(agencyUseCases.getNearbyAgencies(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt()))
                    .thenReturn(page);

            mockMvc.perform(get("/agencies/341")
                            .param("zip-code", "12345678")
                            .param("radius", "5")
                            .param("next-page-token", "token")
                            .param("limit", "10")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray());
        }

        @Test
        void getNearbyAgencies_invalidBankCode() throws Exception {
            mockMvc.perform(get("/agencies/34")
                            .param("zip-code", "12345678")
                            .param("radius", "5")
                            .param("next-page-token", "token")
                            .param("limit", "10")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void getNearbyAgencies_invalidZipCode() throws Exception {
            mockMvc.perform(get("/agencies/341")
                            .param("zip-code", "123")
                            .param("radius", "5")
                            .param("next-page-token", "token")
                            .param("limit", "10")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void getNearbyAgencies_invalidRadius() throws Exception {
            mockMvc.perform(get("/agencies/341")
                            .param("zip-code", "12345678")
                            .param("radius", "3")
                            .param("next-page-token", "token")
                            .param("limit", "10")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void getNearbyAgencies_invalidLimit() throws Exception {
            mockMvc.perform(get("/agencies/341")
                            .param("zip-code", "12345678")
                            .param("radius", "5")
                            .param("next-page-token", "token")
                            .param("limit", "0")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }
}
