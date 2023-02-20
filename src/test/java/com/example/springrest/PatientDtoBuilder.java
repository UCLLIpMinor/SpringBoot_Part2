package com.example.springrest;

import com.example.springrest.patient.web.PatientDto;

public class PatientDtoBuilder {

    private String name;
    private String email;
    private int age;

    private PatientDtoBuilder() {
    }

    public static PatientDtoBuilder aPatient () {
        return new PatientDtoBuilder();
    }

    public static PatientDtoBuilder aPatientElke() {
        return aPatient().withName("Elke").withEmail("Elke@ucll.be").withAge(43);
    }

    public static PatientDtoBuilder anInvalidPatientWithNoName() {
        return aPatient().withName("").withEmail("Johan@ucll.be").withAge(65);
    }

    public static PatientDtoBuilder aPatientGreetje() {
        return aPatient().withName("Greetje").withEmail("Greetje@ucll.be").withAge(47);
    }

    public static PatientDtoBuilder aPatientMiyo() {
        return aPatient().withName("Miyo").withEmail("Miyo@ucll.be").withAge(14);
    }

    public PatientDtoBuilder withName (String name) {
        this.name = name;
        return this;
    }

    public PatientDtoBuilder withEmail (String email) {
        this.email = email;
        return this;
    }

    public PatientDtoBuilder withAge (int age) {
        this.age = age;
        return this;
    }

    public PatientDto build() {
        PatientDto patient = new PatientDto();
        patient.setName(name);
        patient.setEmail(email);
        patient.setAge(age);
        return patient;
    }
}
