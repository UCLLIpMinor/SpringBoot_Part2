package com.example.springrest;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;


@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional // Cleans-up after test is finished
public class PatientEnd2EndTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void validateThatA400IsReturnedWhenANonValidEmailIsUsed() {
        PatientBodyValue value = new PatientBodyValue();
        value.age = 12;
        value.name = "TestName";
        value.email = "testemail";

        client.post()
                .uri("/api/patient/add")
                .bodyValue(value)
                .exchange()
                .expectBody()
                .json("{\"email\":\"email.not.valid\"}");
    }

    private static class PatientBodyValue {

        public String name;
        public String email;
        public Integer age;
    }
}
