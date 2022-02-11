package com.project.demo.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.demo.model.Appointment;
import com.project.demo.repository.AppointmentRepository;

@Service
public class AppointmentServiceImpl implements AppointmentService {

	@Autowired
	private AppointmentRepository appointmentrepository;

	@Override
	public List<Appointment> getAllAppointments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Appointment saveAppointment(Appointment appointmnet) {
		// TODO Auto-generated method stub
		return this.appointmentrepository.save(appointmnet);
	}

	@Override
	public List<Appointment> getAllbyPatient(int patientId, Date date) {
		// TODO Auto-generated method stub
		return appointmentrepository.findAppointmentbyPatient(patientId, date);
	}

	@Override
	public long getAppointmentCount() {
		// TODO Auto-generated method stub
		return appointmentrepository.count();
	}

	@Override
	public List<Appointment> getAllbySchedule(int scheduleId) {
		// TODO Auto-generated method stub
		return appointmentrepository.findAppointmentbySchedule(scheduleId);
	}
	
}
