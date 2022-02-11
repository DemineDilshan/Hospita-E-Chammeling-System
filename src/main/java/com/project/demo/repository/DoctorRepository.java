package com.project.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.demo.model.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

	@Query("SELECT d FROM Doctor d WHERE d.name LIKE %?1%")
	public List<Doctor> findAllByName(String docName);
	
	@Query("SELECT d FROM Doctor d WHERE  d.specialty = ?1")
	public List<Doctor> findAllBySpecilaty(String docSpecialty);
	
	@Query("SELECT d FROM Doctor d WHERE d.name LIKE %?1% OR d.specialty = ?2")
	public List<Doctor> findAllByNameOrSpecilty(String docName,String docSpecialty);
	
	@Query("SELECT d FROM Doctor d WHERE d.name LIKE %?1% AND d.specialty = ?2")
	public List<Doctor> findAllByNameAndSpecilty(String docName,String docSpecialty);
	
	Optional<Doctor> findByemail(String email);
	
	@Query("SELECT COUNT(d) FROM User d")
	int countByDoctor();

	
}
