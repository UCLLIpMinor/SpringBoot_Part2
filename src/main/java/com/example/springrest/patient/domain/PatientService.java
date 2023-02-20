package com.example.springrest.patient.domain;

import com.example.springrest.generic.ServiceException;
import com.example.springrest.patient.web.PatientDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public List<Patient> getPatients() {
        // PagingAndSortingRepository (which is part of JpaRepository)
        // model.addAttribute("patients", patientRepository.findAll(Sort.by("name").ascending()));
        return patientRepository.findAll();
    }

    public Patient getPatient(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ServiceException("get", "no.patient.with.this.id"));
    }

    public Patient createPatient(PatientDto dto) {
        if (patientRepository.findByEmail(dto.getEmail()) != null) {
            throw new ServiceException("add", "email.already.used");
        }

        Patient patient = new Patient();

        patient.setName(dto.getName());
        patient.setEmail(dto.getEmail());
        patient.setAge(dto.getAge());

        return patientRepository.save(patient);
    }

    public Patient updatePatient(Long id, PatientDto dto) {
        Patient patient = getPatient(id);

        patient.setName(dto.getName());
        patient.setEmail(dto.getEmail());
        patient.setAge(dto.getAge());

        return patientRepository.save(patient);
    }

    public void deletePatientWithId(long id) {
        patientRepository.delete(getPatient(id));
    }
}
