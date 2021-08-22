package com.binu.clinicals.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.binu.clinicals.dto.ClinicalDataRequest;
import com.binu.clinicals.entities.ClinicalData;
import com.binu.clinicals.entities.Patient;
import com.binu.clinicals.repositories.ClinicalDataRepository;
import com.binu.clinicals.repositories.PatientRepository;
import com.binu.clinicals.utility.BMICalculator;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class ClinicalDataController {
	
	private ClinicalDataRepository clinicalDataRepository;
	private PatientRepository patientRepository;
		
	@Autowired
	public ClinicalDataController(ClinicalDataRepository clinicalDataRepository, PatientRepository patientRepository) {
		super();
		this.clinicalDataRepository = clinicalDataRepository;
		this.patientRepository = patientRepository;
	}



	// @RequestBody annotation maps the HttpRequest body to a transfer or domain object, enabling 
	// automatic deserialization of the inbound HttpRequest body onto a Java object. 
	// Spring automatically deserializes the JSON into a Java type, assuming an appropriate one is specified.
	@RequestMapping(value = "/clinicals", method=RequestMethod.POST)
	public ClinicalData saveClinicalData(@RequestBody ClinicalDataRequest request) {
		
		// convert from DTO object to ClinicalData object and persist it in the database
		Patient patient = patientRepository.findById(request.getPatientId()).get();
		ClinicalData clinicalData = new ClinicalData();
		clinicalData.setComponentName(request.getComponentName());
		clinicalData.setComponentValue(request.getComponentValue());
		clinicalData.setPatient(patient);
		return clinicalDataRepository.save(clinicalData);
		
	}
	
	@RequestMapping(value="/clinicals/{patientId}/{componentName}", method=RequestMethod.GET)
	public List<ClinicalData> getClinicalData(@PathVariable("patientId") int patientId, @PathVariable("componentName") String componentName) {
		
		if (componentName.equals("bmi")) {
			// need to make this change to componentName so that "hw" can be used in the findByPatientIdAndComponentNameOrderByMeasuredDateTime query
			// to retrieve height/weight records in order to comput BMI
			// Note that BMI does not exist in the database.  It is only for the UI.
			componentName = "hw";
		}
		
		List<ClinicalData> clinicalData = clinicalDataRepository.findByPatientIdAndComponentNameOrderByMeasuredDateTime(patientId,componentName);
		List<ClinicalData> duplicateClinicalData = new ArrayList<>(clinicalData);
		for(ClinicalData eachEntry: duplicateClinicalData) {
			
			BMICalculator.calculateBMI(clinicalData, eachEntry);
		}

		return clinicalData;
	}
	
	
}
