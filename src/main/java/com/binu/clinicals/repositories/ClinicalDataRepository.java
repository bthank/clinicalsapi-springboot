package com.binu.clinicals.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.binu.clinicals.entities.ClinicalData;

public interface ClinicalDataRepository extends JpaRepository<ClinicalData, Integer> {

}
