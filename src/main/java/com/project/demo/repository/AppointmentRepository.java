package com.project.demo.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.demo.model.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

	
	@Query("SELECT a FROM Appointment a WHERE a.patientid = ?1 AND a.appointmentDate >= ?2")
	public List<Appointment> findAppointmentbyPatient(int patientId, Date date);
	
	@Query("SELECT a FROM Appointment a WHERE a.scheduleid = ?1")
	public List<Appointment> findAppointmentbySchedule(int scheduleId);
}
