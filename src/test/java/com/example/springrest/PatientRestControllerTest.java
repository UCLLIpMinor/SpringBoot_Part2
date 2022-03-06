package com.example.springrest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
// @WebMvcTest also auto-configures MockMvc
// which offers a powerful way of easy testing MVC controllers without starting a full HTTP server
@WebMvcTest(PatientRestController.class)
public class PatientRestControllerTest {

    // For creating JSONs from objects
    ObjectMapper mapper = new ObjectMapper();

    // We use @MockBean to create and inject a mock for the service
    @MockBean
    PatientService service;

    // To not start the server at all but to test only the layer below that,
    // where Spring handles the incoming HTTP request and hands it off to your controller.
    // That way, almost of the full stack is used, and your code will be called in exactly the
    // same way as if it were processing a real HTTP request but without the cost of starting the server.
    // To do that, use Spring’s MockMvc and ask for that to be injected for
    // you by using the @WebMvcTest annotation on the test case.
    @Autowired
    MockMvc patientRestController;

    Patient elke, greetje, johan;

    @Before
    public void setUp() {
        elke = PatientBuilder.aPatientElke().build();
        greetje = PatientBuilder.aPatientGreetje().build();
        johan = PatientBuilder.anInvalidPatientWithNoName().build();
    }

    @Test
    public void givenPatients_whenGetRequestToAllPatients_thenJSONwithAllPatientsReturned() throws Exception {
        // given
        List<Patient> patients = Arrays.asList(elke, greetje);

        // mocking
        given(service.findAllPatients()).willReturn(patients);

        // when
        patientRestController.perform(get("/api/patient/all")
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", Is.is(elke.getName())))
                .andExpect(jsonPath("$[1].name", Is.is(greetje.getName())));
    }

    @Test
    public void givenNoPatients_whenPostRequestToAddAValidPatient_thenJSONisReturned() throws Exception {
        // given
        List<Patient> patients = Arrays.asList(elke);

        // mocking
        when(service.addPatient(elke)).thenReturn(elke);
        when(service.findAllPatients()).thenReturn(patients);

        // when
        patientRestController.perform(post("/api/patient/add")
                .content(mapper.writeValueAsString(elke))
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", Is.is(elke.getName())));
    }

    @Test
    public void givenNoPatients_whenPostRequestToAddAnInvalidPatient_thenErrorInJSONformatIsReturned() throws Exception {
        // given

        // when
        patientRestController.perform(post("/api/patient/add")
                .content(mapper.writeValueAsString(johan))
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", Is.is("name.missing")));
    }

}
