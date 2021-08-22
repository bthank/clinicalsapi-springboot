package com.binu.clinicals.utility;

import java.util.List;

import com.binu.clinicals.entities.ClinicalData;

public class BMICalculator {

	public static void calculateBMI(List<ClinicalData> clinicalData, ClinicalData eachEntry) {
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

	
}
