package com.example.springrest;

import com.example.springrest.patient.web.PatientDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class PatientDtoTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
    public void givenValidPatient_shouldHaveNoViolations() {
        //given
        PatientDto elke = PatientDtoBuilder.aPatientElke().build();

        //when
        Set<ConstraintViolation<PatientDto>> violations = validator.validate(elke);

        //then
        assertTrue(violations.isEmpty());
    }

    @Test
    public void givenPatientWithEmptyName_shouldDetectInvalidNameError() {
        //given
        PatientDto johan = PatientDtoBuilder.anInvalidPatientWithNoName().build();

        //when
        Set<ConstraintViolation<PatientDto>> violations = validator.validate(johan);

        //then
        assertEquals(violations.size(), 1);
        ConstraintViolation<PatientDto> violation = violations.iterator().next();
        assertEquals("name.missing", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
    }
}