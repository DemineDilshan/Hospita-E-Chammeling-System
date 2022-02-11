package com.project.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.demo.model.Doctor;
import com.project.demo.repository.DoctorRepository;

@Service
public class DoctorServiceImpl implements DoctorService {

	@Autowired
	private DoctorRepository doctorrepository;

	@Override
	public List<Doctor> getAllDoctors() {
		return doctorrepository.findAll();
	}

	@Override
	public Doctor saveDoctor(Doctor doctor) {
		return this.doctorrepository.save(doctor);
	}

	@Override
	public Doctor getDoctorById(int id) {
		Optional<Doctor> optional = doctorrepository.findById(id);
		Doctor doctor = null;
		if (optional.isPresent()) {
			doctor = optional.get();
		} else {
			doctor = new Doctor();
		}
		return doctor;
	}

	@Override
	public void deleteDoctorbyID(int id) {
		// TODO Auto-generated method stub
		this.doctorrepository.deleteById(id);
	}

	@Override
	public List<Doctor> getAllDoctorsByName(String docName) {
		if (docName != null) {
			return doctorrepository.findAllByName(docName);
		} else {
			return doctorrepository.findAll();
		}

	}

	@Override
	public List<Doctor> getAllDoctorsByName(String docName, String docSpecialty) {
		if (docName != null) {
			if (!docSpecialty.equals("")) {
				return doctorrepository.findAllByNameAndSpecilty(docName, docSpecialty);
				
			} else {
				return doctorrepository.findAllByNameOrSpecilty(docName, docSpecialty);
			}
		} else {
			if (!docSpecialty.equals("")) {
				return doctorrepository.findAllByNameOrSpecilty(docName, docSpecialty);
			} else {
				return doctorrepository.findAll();
			}
			
		}

	}

	@Override
	public Doctor getDcotorByemail(String email) {
		Optional<Doctor> optional = doctorrepository.findByemail(email);
		Doctor doctor = null;
		if (optional.isPresent()) {
			doctor = optional.get();
		}
		else {
			doctor = new Doctor();
		}
		return doctor;
	}

	@Override
	public long getDoctorCount() {
		// TODO Auto-generated method stub
		return doctorrepository.count();
	}

	@Override
	public List<Doctor> getAllDoctorsBySpecialty(String docSpecialty) {
		// TODO Auto-generated method stub
		return doctorrepository.findAllBySpecilaty(docSpecialty);
	}

}
