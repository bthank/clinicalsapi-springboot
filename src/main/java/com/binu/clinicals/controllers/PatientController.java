package com.binu.clinicals.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.binu.clinicals.entities.Patient;
import com.binu.clinicals.repositories.PatientRepository;

@RestController
@RequestMapping("/api")
public class PatientController {

	
	private PatientRepository patientRepository;
	
	@Autowired
	public PatientController(PatientRepository patientRepository) {
		this.patientRepository = patientRepository;
	}



	@RequestMapping(value="/patients", method=RequestMethod.GET)
	public List<Patient> getPatients(){
		
		return patientRepository.findAll();
		
	}
	
	
	@RequestMapping(value="/patients/{id}")
	public Patient getPatient(@PathVariable("id") int id) {
		
		return patientRepository.findById(id).get();
	
	}
	
	@RequestMapping(value="/patients",method=RequestMethod.POST)
	public Patient savePatient(@RequestBody Patient patient) {
		
		return patientRepository.save(patient);
	}
	
	
	
}
