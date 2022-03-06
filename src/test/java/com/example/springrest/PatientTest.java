package com.example.springrest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PatientTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeClass
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterClass
    public static void close() {
        validatorFactory.close();
    }

    @Test
    public void givenValidPatient_shouldHaveNoViolations() {
        //given
        Patient elke = PatientBuilder.aPatientElke().build();

        //when
        Set<ConstraintViolation<Patient>> violations = validator.validate(elke);

        //then
        assertTrue(violations.isEmpty());
    }

    @Test
    public void givenPatientWithEmptyName_shouldDetectInvalidNameError() {
        //given
        Patient johan = PatientBuilder.anInvalidPatientWithNoName().build();

        //when
        Set<ConstraintViolation<Patient>> violations = validator.validate(johan);

        //then
        assertEquals(violations.size(), 1);
        ConstraintViolation<Patient> violation = violations.iterator().next();
        assertEquals("name.missing", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
    }
}