package com.binu.clinicals.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.binu.clinicals.entities.ClinicalData;
import com.binu.clinicals.entities.Patient;
import com.binu.clinicals.repositories.PatientRepository;

@RestController
@RequestMapping("/api")
public class PatientController {

	Map<String,String> filters = new HashMap<>();
	
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
	
	/**
	 * REST method that finds the patient based on the patient id passed in, fetches the patient clinical data
	 * from the patient record, duplicates the patient clinical data,  then loops through the 
	 * patient clinical data and manipulates the data to calculate the patient's BMI and stores it into
	 * the Clinical Data table.
	 * 
	 * BMI = weight / (heightinmetres^2)
	 * 
	 * @param id
	 * @return Patient
	 */
	@RequestMapping(value="/patients/analyze/{id}",method=RequestMethod.GET)
	public Patient analyze(@PathVariable("id") int id) {
		
		Patient patient = patientRepository.findById(id).get();
		List<ClinicalData> clinicalData = patient.getClinicalData();
		List<ClinicalData> duplicateClinicalData = new ArrayList<>(clinicalData);
		for(ClinicalData eachEntry: duplicateClinicalData) {
			
			// Use this filters map to only process the first record of each component type and
			// exclude the 2nd and subsequent records of each type.
			// Initially the filters map will be empty.
			if(filters.containsKey(eachEntry.getComponentName())) {
				clinicalData.remove(eachEntry);
				continue;
			} else {
				filters.put(eachEntry.getComponentName(), null);
			}

			
			if (eachEntry.getComponentName().equals("hw")) {
					
				// use a regular expression to split the component value ("ht/wt") into separate height and weight strings
				// array index 0 = height
				// array index 1 = weight
				String[] heightAndWeight = eachEntry.getComponentValue().split("/");
				if (heightAndWeight != null && heightAndWeight.length > 1) {  // heightAndWeight must have 2 elements
					String heightString = heightAndWeight[0];
					String weightString = heightAndWeight[1];
					
					float heightInMetres = Float.parseFloat(heightString) * 0.4536F;
					float weight = Float.parseFloat(weightString);
					// float bmi = weight / (heightInMetres * heightInMetres);
					float bmi = (float) ((float) weight / Math.pow(heightInMetres, 2));
					ClinicalData bmiData = new ClinicalData();
					
					// add a new clinical data component type of bmi
					bmiData.setComponentName("bmi");
					// bmi is stored as a string in the Clinical Data record, so convert the float value to a string
					bmiData.setComponentValue(Float.toString(bmi));
					
					// now add the new entry to the patient's clinical data
					clinicalData.add(bmiData);
				}
				
			}
		}
		// clear the filters map at the end of each analyis otherwise it will remain populated the next time
		// analyze is called
		filters.clear();
		return patient;
	}
	
}
