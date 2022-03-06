package com.example.springrest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public Patient addPatient(Patient patient) throws ServiceException {
        if (patientRepository.findByEmail(patient.getEmail()) != null){
            throw new ServiceException("add", "email.already.used");
        }
        return patientRepository.save(patient);
    }

    public Iterable<Patient> findAllPatients() {
        return patientRepository.findAll();
    }

    public void deletePatientWithId(long id) {
        patientRepository.delete(patientRepository.findById(id).orElseThrow(()->new ServiceException("delete", "no.patient.with.this.id")));
    }

}
