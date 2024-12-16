package br.com.santander.nearagencyapi.interfaces;

import br.com.santander.nearagencyapi.application.usecases.AgencyUseCases;
import br.com.santander.nearagencyapi.factory.AgencyFactory;
import br.com.santander.nearagencyapi.interfaces.dto.AgencyDto;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

        private AgencyDto agencyDto;

        @Test
        void createAgency() throws Exception {
            Mockito.doNothing().when(agencyUseCases).createAgency(Mockito.any());

            mockMvc.perform(post("/agencies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(AgencyFactory.createDefaultAgencyDto())))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "http://localhost/agencies/09111410/2783"));
        }

        @Test
        void createAgencyWithInvalidData() throws Exception {
            agencyDto = AgencyFactory.createDefaultAgencyDto();
            agencyDto.setAgencyZipCode(null);

            mockMvc.perform(post("/agencies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(agencyDto)))
                    .andExpect(status().isBadRequest());
        }
        @Test
        void createAgencyWithInvalidZipCode() throws Exception {
            agencyDto = AgencyFactory.createWithInvalidZipCode();

            mockMvc.perform(post("/agencies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(agencyDto)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void createAgencyWithInvalidEmail() throws Exception {
            agencyDto = AgencyFactory.createWithInvalidEmail();

            mockMvc.perform(post("/agencies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(agencyDto)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void createAgencyWithMissingFields() throws Exception {
            agencyDto = AgencyFactory.createWithMissingFields();

            mockMvc.perform(post("/agencies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(agencyDto)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void createAgencyWithInvalidDataProblemDetail() throws Exception {
            agencyDto = AgencyFactory.createDefaultAgencyDto();
            agencyDto.setAgencyZipCode("091114100"); // Invalid zip code

            mockMvc.perform(post("/agencies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(agencyDto)))
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
            agencyDto = AgencyFactory.createWithInvalidZipCode();

            mockMvc.perform(post("/agencies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(agencyDto)))
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
            agencyDto = AgencyFactory.createWithInvalidEmail();

            mockMvc.perform(post("/agencies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(agencyDto)))
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
            agencyDto = AgencyFactory.createWithMissingFields();

            mockMvc.perform(post("/agencies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(agencyDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.type").value("about:blank"))
                    .andExpect(jsonPath("$.title").value("Validation error"))
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.detail").value("One or more parameters are invalid"))
                    .andExpect(jsonPath("$.instance").value("/agencies"));
        }
    }
}
