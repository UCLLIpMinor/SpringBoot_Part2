package com.example.springrest;

import com.example.springrest.patient.domain.Patient;
import com.example.springrest.patient.domain.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class PatientRepositoryTest {

    // TestEntityManager allows to use EntityManager in tests.
    // Spring Repository is an abstraction over EntityManager;
    // it shields developers from lower-level details of JPA
    // and brings many convenient methods. But Spring allows
    // to use EntityManager when needed in application code and tests.
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PatientRepository patientRepository;

    // onnodig om deze te testen aangezien die door JPA wordt gedaan
    // enkel ter illustratie toegevoegd hier
    @Test
    public void givenPatientRegistered_whenFindByEmail_thenReturnPatient() {
        // given
        Patient elke = PatientBuilder.aPatientElke().build();
        entityManager.persistAndFlush(elke);
        Patient greetje = PatientBuilder.aPatientGreetje().build();
        entityManager.persistAndFlush(greetje);

        // when
        Patient found = patientRepository.findByEmail(elke.getEmail());

        // then
        assertNotNull(found);
        assertThat(found.getName()).isEqualTo(elke.getName());
    }

    // @Query moet je wel testen omdat het een query is die je zelf hebt gemaakt
    @Test
    public void givenPatientRegistered_whenFindByAdults_thenAllAdultsAreReturned() {
        // given
        Patient elke = PatientBuilder.aPatientElke().build();
        entityManager.persistAndFlush(elke);
        Patient greetje = PatientBuilder.aPatientGreetje().build();
        entityManager.persistAndFlush(greetje);
        Patient miyo = PatientBuilder.aPatientMiyo().build();
        entityManager.persistAndFlush(miyo);

        // when
        List<Patient> adults = patientRepository.findAllAdults();

        // then
        assertEquals(2, adults.size());
    }
}
