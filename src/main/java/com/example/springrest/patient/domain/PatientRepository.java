package com.example.springrest.patient.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findAllByEmail(String email);

    Patient findByEmail(String email);

    @Query("SELECT p FROM Patient p WHERE p.age>18")
    List<Patient> findAllAdults ();
    // Better to do this with following method
    // The above was to demonstrate the @Query:
    // so that you can write your own queries when JPA doesn't has the method you need
    // List<Patient> findPatientsByAgeAfter (int age);
}
