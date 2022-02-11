package com.project.demo.controller;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.demo.model.Appointment;
import com.project.demo.model.Patient;
import com.project.demo.model.Schedule;
import com.project.demo.service.AppointmentService;
import com.project.demo.service.DoctorService;
import com.project.demo.service.PatientService;
import com.project.demo.service.ScheduleService;

@Controller
public class PatientPageController {

	String currentPassword = null, confirmPassword = null, newPassword = null;
	String invalidEmail = null;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private AppointmentService appointmentservice;

	@Autowired
	PatientLoginController plc;

	@Autowired
	private DoctorService doctorservice;

	@Autowired
	private ScheduleService scheduleservice;

	@Autowired
	private PatientService patientservice;
	
	@GetMapping("/PatientLogOut")
	public String patientLogOut() {
		return "patient_LogIn";
	}

//	@GetMapping("/ViewMainHome")
	public String displayHome() {
		System.out.println(plc.pid);
		return "patient_Home";
	}

	@GetMapping("/ViewMainDoctor")
	public String displayDoctor(Model model) {
		model.addAttribute("listDoctor", doctorservice.getAllDoctors());
		return "patient_Doctors";
	}

	@GetMapping("/SearchDoctor")
	public String searchDoctor(Model model, @Param("docName") String docName,
			@Param("docSpecialty") String docSpecialty) {

		if (docSpecialty.equals("--Select--")) {
			docSpecialty = "";
		}

		model.addAttribute("listDoctor", doctorservice.getAllDoctorsByName(docName, docSpecialty));
		model.addAttribute("docName", docName);

		if (docSpecialty.equals("")) {
			docSpecialty = "--Select--";
		}

		model.addAttribute("docSpecialty", docSpecialty);
		System.out.println(docSpecialty);
		return "patient_Doctors";
	}

	@GetMapping("/ScheduleAppointment")
	public String displayScheduleAppointment(Model model, @Param("docid") Integer docid) {
		System.out.println("asdsad" + docid);
		LocalDate localdate = LocalDate.now();
		Date date1 = Date.valueOf(localdate);
		model.addAttribute("listSchedule", scheduleservice.getAllSchedules(docid, date1));
		return "patient_Doctors_Schedule";
	}

	@GetMapping("/ViewMainAppointment")
	public String displayAppointment(Model model) {

		LocalDate localdate = LocalDate.now();
		Date date1 = Date.valueOf(localdate);

		if (!String.valueOf(plc.pid).equals(null)) {
			model.addAttribute("listAppointment", appointmentservice.getAllbyPatient(plc.pid, date1));
		}

		return "patient_Appointments";
	}

	@GetMapping("/ViewMainContact")
	public String displayAbout() {
		return "patient_Contact";
	}

	@GetMapping("/ViewMainAccount")
	public String displayAccount(Model model) {

		this.currentPassword = null;
		this.confirmPassword = null;
		this.newPassword = null;

		System.out.println(plc.pid);
		Patient patient = patientservice.getPatientById(plc.pid);
		model.addAttribute("name", patient.getName());
		model.addAttribute("address", patient.getAddress());
		model.addAttribute("email", patient.getEmail());
		model.addAttribute("telephone", patient.getTelephone());
		return "patient_Account";
	}

	@GetMapping("/EditProfiel")
	public String displayEditAccount(Model model) {

		this.currentPassword = null;
		this.confirmPassword = null;
		this.newPassword = null;

		Patient patient = patientservice.getPatientById(plc.pid);
		model.addAttribute("id", patient.getId());
		model.addAttribute("name", patient.getName());
		model.addAttribute("address", patient.getAddress());
		model.addAttribute("email", patient.getEmail());
		model.addAttribute("telephone", patient.getTelephone());
		model.addAttribute("password", patient.getPassword());
		return "patient_Account_EditProfile ";
	}

	@GetMapping("/ChangePassword")
	public String displayChangePasword(Model model) {

		Patient patient = patientservice.getPatientById(plc.pid);
		model.addAttribute("id", patient.getId());
		model.addAttribute("name", patient.getName());
		model.addAttribute("address", patient.getAddress());
		model.addAttribute("email", patient.getEmail());
		model.addAttribute("telephone", patient.getTelephone());
		model.addAttribute("password", patient.getPassword());
		model.addAttribute("currentpassword", currentPassword);
		model.addAttribute("newpassword", newPassword);
		model.addAttribute("confirmpassword", confirmPassword);
		System.out.println(currentPassword);
		System.out.println(newPassword);
		System.out.println(confirmPassword);
		return "patient_Account_ChagePass";
	}

	@PostMapping("/UpdateProfile")
	public String updateProfile(@ModelAttribute("patient") Patient patient,Model model) {
		Patient patient1 = patientservice.getPatientByemail(patient.getEmail());
		
		patientservice.savePatient(patient);
		return "redirect:/ViewMainAccount";
		
//		if(patient1.getEmail().equals(patient.getEmail())) {
//			this.invalidEmail = patient1.getEmail();
//			return "redirect:/EditProfiel?InvalidEmail";
//		}
//		else {
//			
//		}
	}

	@PostMapping("/UpdatePssword")
	public String updatePassword(@RequestParam("currentpassword") String currentPass,
			@RequestParam("newpassword") String newPass, @RequestParam("confirmpassword") String confirmPass,
			@ModelAttribute("patient") Patient patient) {

		if (!currentPass.equals(patient.getPassword())) {
			this.currentPassword = null;
			this.confirmPassword = confirmPass;
			this.newPassword = newPass;
			System.out.println(currentPassword);
			System.out.println(newPassword);
			System.out.println(confirmPassword);
			return "redirect:/ChangePassword?InvalidCurrentPass";
		} else if (!newPass.equals(confirmPass)) {
			this.currentPassword = currentPass;
			this.confirmPassword = null;
			this.newPassword = newPass;
			return "redirect:/ChangePassword?InvalidConfirmPass";
		} else {
			this.currentPassword = null;
			this.confirmPassword = null;
			this.newPassword = null;
			patient.setPassword(newPass);
			patientservice.savePatient(patient);
			return "redirect:/ViewMainAccount";
		}

	}

	@GetMapping("/MakeAppointment")
	public String makeAppointment(int id) throws UnsupportedEncodingException, MessagingException {
		System.out.println(id);
		System.out.println(plc.pid);
		Patient patient = patientservice.getPatientById(plc.pid);
		Schedule schedule = scheduleservice.getScheduleById(id);

		int pcnum = Integer.valueOf(schedule.getPatientCount());
		int avnum = Integer.valueOf(schedule.getAvailableNumber());
		int apnum = 0;

		if (avnum != 0) {
			avnum = avnum - 1;
			apnum = pcnum - avnum;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Calendar c = Calendar.getInstance();
		System.out.println(sdf.format(schedule.getTime()));
		try {
			c.setTime(sdf.parse(String.valueOf(schedule.getTime())));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.add(Calendar.MINUTE, apnum * 15);
		System.out.println(sdf.format(c.getTime()));
		String date = sdf.format(c.getTime());
		Time time = Time.valueOf(date);

		Appointment appointment = new Appointment();

		appointment.setScheduleid(id);
		appointment.setAppointmentTime(time);
		appointment.setAppointmentNumber(apnum);
		appointment.setPatientid(plc.pid);
		appointment.setAppointmentDate(schedule.getDate());

		appointmentservice.saveAppointment(appointment);

		schedule.setAvailableNumber(String.valueOf(avnum));
		scheduleservice.saveSchedule(schedule);

		System.out.println(apnum);
		System.out.println("this time fsd" + time);
		System.out.println(appointment.getAppointmentTime());

		senEmail(patient.getEmail(),appointment,patient,schedule);

		return "redirect:/ViewMainAppointment";
	}

	private void senEmail(String email, Appointment appointment, Patient patient, Schedule schedule)
			throws UnsupportedEncodingException, MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("dilshanpromoday10@gmail.com", "Viveka Support");
		helper.setTo(email);

		String subject = "Viveka Hospital Appointment";

		String content = "<p>Hello, " + patient.getName() + "</p>" 
				+ "<p>Your appointment date is " + appointment.getAppointmentDate() + "</p>" 
				+ "<p>Your appointment number and time are " + appointment.getAppointmentNumber() +" and "+ appointment.getAppointmentTime() +"</p>"
				+ "<p>Doctor Arrives at " + schedule.getTime() + "</p>" 
				+ "<p>Please be kind to arrive befor your appointment time</p>"
				+ "<p>Thank You!</p>";

		helper.setSubject(subject);
		helper.setText(content, true);

		mailSender.send(message);
	}
}
