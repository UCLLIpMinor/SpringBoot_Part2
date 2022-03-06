package com.example.springrest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class PatientController {

    @Autowired
    private PatientService service;

    @GetMapping("/overview")
    public String overview (Model model) {
        /*
        Patient elke = new Patient();
        elke.setName("Elke");
        elke.setEmail("elke.steegmans@ucll.be");
        elke.setAge(44);
        Patient miyo = new Patient();
        miyo.setName("Miyo");
        miyo.setEmail("miyo.vandenende@ucll.be");
        patientRepository.save(elke);
        patientRepository.save(miyo);
         */
        // CrudRepository (which is part of JpaRepository)
         model.addAttribute("patients", service.findAllPatients());
        // PagingAndSortingRepository (which is part of JpaRepository)
        //model.addAttribute("patients", patientRepository.findAll(Sort.by("name").ascending()));
        return "overview-patient";
    }

    @GetMapping("/home")
    public String home() {
        return "index";
    }

    @GetMapping("/add")
    public String add(Patient patient) {
        return "add-patient";
    }

    @PostMapping("/add")
    public String add(@Valid Patient patient, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-patient";
        }
        try {
            service.addPatient(patient);
        }
        catch (ServiceException exc) {
            model.addAttribute("error", exc.getMessage());
            return "add-patient";
        }
        return "redirect:/overview";
    }
}
