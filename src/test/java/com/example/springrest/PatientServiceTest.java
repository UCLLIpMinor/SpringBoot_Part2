package com.example.springrest;

import com.example.springrest.generic.ServiceException;
import com.example.springrest.patient.domain.Patient;
import com.example.springrest.patient.domain.PatientRepository;
import com.example.springrest.patient.domain.PatientService;
import com.example.springrest.patient.web.PatientDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    PatientRepository patientRepository;

    @InjectMocks
    PatientService patientService;

    @Test
    public void givenNoPatients_whenValidPatientAdded_ThenPatientIsAddedAndPatientIsReturned() {
        // given
        Patient elke = PatientBuilder.aPatientElke().build();
        PatientDto elkeDto = PatientDtoBuilder.aPatientElke().build();

        // mock all methods that are called in method that is tested here
        // PatientService addPatient
        // because otherwhise the PatientRepository of PatientService will be used
        when(patientRepository.findByEmail(elke.getEmail())).thenReturn(null);
        when(patientRepository.save(any())).thenReturn(elke);

        // when
        Patient addedPatient = patientService.createPatient(elkeDto);

        // then
        assertThat(elke.getName()).isSameAs(addedPatient.getName());
    }

    @Test // (expected = ServiceException.class)
    // THIS IS NOT ENGOUGH BECAUSE THEN YOU DON'T KNOW IF RIGHT MESSAGE IS THROWN
    public void givenPatients_whenValidPatientAddedWithAlreadyUsedEmail_ThenPatientIsNotAddedAndErrorIsReturned() {
        // given
        Patient elke = PatientBuilder.aPatientElke().build();
        PatientDto elkeDto = PatientDtoBuilder.aPatientElke().build();

        when(patientRepository.findByEmail(elke.getEmail())).thenReturn(elke);

        // when
        final Throwable raisedException = catchThrowable(() -> patientService.createPatient(elkeDto));

        // then
        assertThat(raisedException).isInstanceOf(ServiceException.class)
                .hasMessageContaining("email.already.used");
    }
}