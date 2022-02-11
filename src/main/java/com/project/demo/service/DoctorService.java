package com.project.demo.service;

import java.util.List;

import com.project.demo.model.Doctor;

public interface DoctorService {

	List<Doctor> getAllDoctors();
	List<Doctor> getAllDoctorsByName(String docName);
	List<Doctor> getAllDoctorsBySpecialty(String docSpecialty);
	List<Doctor> getAllDoctorsByName(String docName,String docSpecialty);
	Doctor saveDoctor(Doctor doctor);
	Doctor getDoctorById(int id);
	void deleteDoctorbyID(int id);
	Doctor getDcotorByemail(String email);

	long getDoctorCount();
}
