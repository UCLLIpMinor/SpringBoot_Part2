package com.example.springrest;

public class PatientBuilder {

    private String name;
    private String email;
    private int age;

    private PatientBuilder () {
    }

    public static PatientBuilder aPatient () {
        return new PatientBuilder();
    }

    public static PatientBuilder aPatientElke() {
        return aPatient().withName("Elke").withEmail("Elke@ucll.be").withAge(43);
    }

    public static PatientBuilder anInvalidPatientWithNoName() {
        return aPatient().withName("").withEmail("Johan@ucll.be").withAge(65);
    }

    public static PatientBuilder aPatientGreetje() {
        return aPatient().withName("Greetje").withEmail("Greetje@ucll.be").withAge(47);
    }

    public static PatientBuilder aPatientMiyo() {
        return aPatient().withName("Miyo").withEmail("Miyo@ucll.be").withAge(14);
    }

    public PatientBuilder withName (String name) {
        this.name = name;
        return this;
    }

    public PatientBuilder withEmail (String email) {
        this.email = email;
        return this;
    }

    public PatientBuilder withAge (int age) {
        this.age = age;
        return this;
    }

    public Patient build() {
        Patient patient = new Patient();
        patient.setName(name);
        patient.setEmail(email);
        patient.setAge(age);
        return patient;
    }
}
