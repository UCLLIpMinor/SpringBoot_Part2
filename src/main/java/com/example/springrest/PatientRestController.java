package com.example.springrest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/patient")
public class PatientRestController {

    @Autowired
    private PatientService service;

    @GetMapping("/all")
    public Iterable<Patient> getAll (){
        return service.findAllPatients();
    }

    @DeleteMapping("/delete/{id}")
    public void delete (@PathVariable("id") long id) {
        service.deletePatientWithId(id);
    }

    @PostMapping("/add")
    public Iterable<Patient> add (@Valid @RequestBody Patient patient) {
        service.addPatient(patient);
        return service.findAllPatients();
    }

    // add with not @Valid patient is BAD_REQUEST and is redirected to this method (MethodArgumentNotValidException)
    // ServiceException are also redirected to this method
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, ServiceException.class, ResponseStatusException.class})
    public Map<String, String> handleValidationExceptions(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        if (ex instanceof MethodArgumentNotValidException) {
            ((MethodArgumentNotValidException)ex).getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
        }
        else if (ex instanceof ServiceException) {
            errors.put(((ServiceException) ex).getAction(), ex.getMessage());
        }
        else {
            errors.put(((ResponseStatusException)ex).getReason(), ex.getCause().getMessage());
        }
        return errors;
    }

}
