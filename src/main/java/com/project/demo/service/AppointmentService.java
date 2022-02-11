package com.project.demo.service;

import java.sql.Date;
import java.util.List;

import com.project.demo.model.Appointment;

public interface AppointmentService {

	List<Appointment> getAllAppointments();
	List<Appointment> getAllbyPatient(int patientId, Date date);
	List<Appointment> getAllbySchedule(int scheduleId);
	Appointment saveAppointment(Appointment appointmnet);
	
	long getAppointmentCount();
}
