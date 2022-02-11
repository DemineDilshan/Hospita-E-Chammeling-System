package com.project.demo.service;

import java.util.List;

import com.project.demo.model.Patient;

public interface PatientService {

	List<Patient> getAllPatient();
	Patient savePatient(Patient patient);
	Patient getPatientById(int id);
	void deletePatientbyID(int id);
	Patient getPatientByemail(String email);

	long getPatientCount();
}
