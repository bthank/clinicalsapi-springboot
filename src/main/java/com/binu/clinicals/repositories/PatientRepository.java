package com.binu.clinicals.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.binu.clinicals.entities.Patient;

public interface PatientRepository extends JpaRepository<Patient, Integer> {

}
