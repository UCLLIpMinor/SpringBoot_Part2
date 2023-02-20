package com.example.springrest;

import com.example.springrest.patient.domain.Patient;
import com.example.springrest.patient.domain.PatientService;
import com.example.springrest.patient.web.PatientDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpringBasicsApplication.class)
@AutoConfigureMockMvc
public class PatientRestControllerTest {

    // For creating JSONs from objects
    private ObjectMapper mapper = new ObjectMapper();

    // We use @MockBean to create and inject a mock for the service
    @MockBean
    private PatientService service;

    @Autowired
    private WebApplicationContext context;

    // To not start the server at all but to test only the layer below that,
    // where Spring handles the incoming HTTP request and hands it off to your controller.
    // That way, almost of the full stack is used, and your code will be called in exactly the
    // same way as if it were processing a real HTTP request but without the cost of starting the server.
    // To do that, use Spring’s MockMvc and ask for that to be injected for
    // you by using the @WebMvcTest annotation on the test case.
    @Autowired
    MockMvc patientRestController;

    private Patient elke, greetje, johan;
    private PatientDto elkeDto, greetjeDto, johanDto;

    @BeforeEach
    public void setUp() {
        elke = PatientBuilder.aPatientElke().build();
        greetje = PatientBuilder.aPatientGreetje().build();
        johan = PatientBuilder.anInvalidPatientWithNoName().build();

        elkeDto = PatientDtoBuilder.aPatientElke().build();
        greetjeDto = PatientDtoBuilder.aPatientGreetje().build();
        johanDto = PatientDtoBuilder.anInvalidPatientWithNoName().build();
    }

    @Test
    public void givenPatients_whenGetRequestToAllPatients_thenJSONwithAllPatientsReturned() throws Exception {
        // given
        List<Patient> patients = Arrays.asList(elke, greetje);

        // mocking
        given(service.getPatients()).willReturn(patients);

        // when
        patientRestController.perform(get("/api/patient/all")
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", Is.is(elkeDto.getName())))
                .andExpect(jsonPath("$[1].name", Is.is(greetjeDto.getName())));
    }

    @Test
    public void givenNoPatients_whenPostRequestToAddAValidPatient_thenJSONisReturned() throws Exception {
        // given
        List<Patient> patients = Arrays.asList(elke);

        // mocking
        when(service.createPatient(elkeDto)).thenReturn(elke);
        when(service.getPatients()).thenReturn(patients);

        // when
        patientRestController.perform(post("/api/patient/add")
                .content(mapper.writeValueAsString(elkeDto))
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", Is.is(elkeDto.getName())));
    }

    @Test
    public void givenNoPatients_whenPostRequestToAddAnInvalidPatient_thenErrorInJSONformatIsReturned() throws Exception {
        // given

        // when
        patientRestController.perform(post("/api/patient/add")
                .content(mapper.writeValueAsString(johanDto))
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", Is.is("name.missing")));
    }
}
