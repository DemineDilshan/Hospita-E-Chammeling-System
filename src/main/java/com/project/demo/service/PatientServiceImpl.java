package com.project.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.demo.model.Patient;
import com.project.demo.repository.PatientRepository;

@Service
public class PatientServiceImpl implements PatientService {

	@Autowired
	private PatientRepository patientrepository;
	
	@Override
	public List<Patient> getAllPatient() {
		return patientrepository.findAll();
	}

	@Override
	public Patient savePatient(Patient patient) {
		return this.patientrepository.save(patient);
	}

	@Override
	public Patient getPatientById(int id) {
		Optional<Patient> optional = patientrepository.findById(id);
		Patient patient = null;
		if (optional.isPresent()) {
			patient = optional.get();
		} else {
			patient = new Patient();
		}
		return patient;
	}

	@Override
	public void deletePatientbyID(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Patient getPatientByemail(String email) {
		Optional<Patient> optional = patientrepository.findByemail(email);
		Patient patient = null;
		if (optional.isPresent()) {
			patient = optional.get();
		}
		else {
			patient = new Patient();
		}
		return patient;
	}

	@Override
	public long getPatientCount() {
		// TODO Auto-generated method stub
		return patientrepository.count();
	}

}
