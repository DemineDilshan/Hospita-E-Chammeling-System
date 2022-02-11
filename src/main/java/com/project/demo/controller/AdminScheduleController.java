package com.project.demo.controller;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.project.demo.model.Doctor;
import com.project.demo.model.Schedule;
import com.project.demo.service.AppointmentService;
import com.project.demo.service.DoctorService;
import com.project.demo.service.ScheduleService;

@Controller
public class AdminScheduleController {

	@Autowired
	private AppointmentService appointmentservice;
	
	@Autowired
	private ScheduleService scheduleservice;
	
	@Autowired
	private DoctorService doctorservice;
	
	@GetMapping("/AddScheduleForm")
	public String displayAddSchedule(Model model, @Param("docName") String docName) throws ParseException {
		
		//Time Increment
		LocalTime localtime = LocalTime.now();
		Time time = Time.valueOf(localtime);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	    Calendar c = Calendar.getInstance();
	    System.out.println(sdf.format(c.getTime()));
	    c.setTime(sdf.parse(String.valueOf(time)));
		c.add(Calendar.MINUTE, 15);
		System.out.println(sdf.format(c.getTime()));
		
		
		//Pass today's date to input type=date
		LocalDate localdate = LocalDate.now();
		model.addAttribute("minDate", localdate);
		
		model.addAttribute("listDoctor", doctorservice.getAllDoctorsByName(docName));
		model.addAttribute("docName", docName);
		return "admin_AddSchedule";
		
	}
	
	@PostMapping("/AddScheduleForm/saveSchedule")
	public String saveSchedule(@ModelAttribute("schedule") Schedule schedule) {
		
		schedule.setAvailableNumber(schedule.getPatientCount());
		
		scheduleservice.saveSchedule(schedule);
		return "redirect:/AddScheduleForm";
	}
	
	
	@GetMapping("/ViewScheduleForm")
	public String displaySchedule(Model model) {
		
		model.addAttribute("listSchedule", scheduleservice.getAllSchedules());
		return "admin_Schedule";
	}
	
	@GetMapping("/activeSchedules")
	public String findActiveSchedules(Model model) {
		LocalDate localdate = LocalDate.now();
		Date date1 = Date.valueOf(localdate);
		//model.addAttribute("listSchedule", scheduleservice.getAllSchedules(4,date1));
		model.addAttribute("listSchedule", scheduleservice.getActiveSchedules(date1));
		return "admin_Schedule";
	}
	
	@GetMapping("/inactiveSchedules")
	public String findInactiveSchedules(Model model) {
		LocalDate localdate = LocalDate.now();
		Date date1 = Date.valueOf(localdate);
		//model.addAttribute("listSchedule", scheduleservice.getAllSchedules(4,date1));
		model.addAttribute("listSchedule", scheduleservice.getInactiveSchedules(date1));
		return "admin_Schedule";
	}
	
	@GetMapping("/addScheduleForm")
	public String displayAddScheduleForm(int id, Model model) {
		LocalDate localdate = LocalDate.now();
		model.addAttribute("minDate", localdate);
		
		
		Doctor doctor = doctorservice.getDoctorById(id);
		model.addAttribute("doctorId",doctor.getId());
		model.addAttribute("doctorName",doctor.getName());
		model.addAttribute("doctorSpecialty",doctor.getSpecialty());
		return "admin_AddScheduleForm";
	}
	
	@GetMapping("/ViewScheduleAppointments")
	public String displayDoctorAppointment(Model model, int id) {
			model.addAttribute("listAppointment", appointmentservice.getAllbySchedule(id));
			
		
		return "admin_Appointment";
	}
}
