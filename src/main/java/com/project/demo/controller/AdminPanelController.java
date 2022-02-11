package com.project.demo.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.lowagie.text.DocumentException;
import com.project.demo.model.Doctor;
import com.project.demo.model.Patient;
import com.project.demo.model.Schedule;
import com.project.demo.model.User;
import com.project.demo.pdfExporter.ActiveShedulePDFExporter;
import com.project.demo.pdfExporter.DoctorPDFExporter;
import com.project.demo.pdfExporter.PatientPDFExporter;
import com.project.demo.service.AppointmentService;
import com.project.demo.service.DoctorService;
import com.project.demo.service.PatientService;
import com.project.demo.service.ScheduleService;
import com.project.demo.service.UserService;

@Controller
public class AdminPanelController {

	int uid;
	

	@Autowired
	private DoctorService doctorService;

	@Autowired
	private PatientService patienteservice;

	@Autowired
	private UserService userservice;

	@Autowired
	private ScheduleService scheduleservice;

	@Autowired
	private AppointmentService appointmentservice;

	// temp
	@GetMapping("/")
	public String displayHome(Model model) {
		model.addAttribute("doctorCount", doctorService.getDoctorCount());
		model.addAttribute("patientCount", patienteservice.getPatientCount());
		model.addAttribute("scheduleCount", scheduleservice.getScheduleCount());
		model.addAttribute("appointmentCount", appointmentservice.getAppointmentCount());
		return "admin_AdminPanelHome";
	}

	@GetMapping("/AdminPanelHomeForm")
	public String displayAdminPanelHome(Model model) {

		model.addAttribute("doctorCount", doctorService.getDoctorCount());
		model.addAttribute("patientCount", patienteservice.getPatientCount());
		model.addAttribute("scheduleCount", scheduleservice.getScheduleCount());
		model.addAttribute("appointmentCount", appointmentservice.getAppointmentCount());
		return "admin_AdminPanelHome";
	}

	@GetMapping("/ViewPatientForm")
	public String displayPatient(Model model) {
		model.addAttribute("listPatient", patienteservice.getAllPatient());
		return "admin_Patient";
	}

	@GetMapping("/ViewAppointmentForm")
	public String displayAppointment() {
		return "admin_Appointment";
	}

	@GetMapping("/AdminLogout")
	public String adminLogout() {
		return "admin_Login";
	}

	@GetMapping("/ViewDoctor")
	public String displayDocotrs(Model model) {
		model.addAttribute("listDoctor", doctorService.getAllDoctors());
		return "admin_Doctor";
	}

	@PostMapping("/ViewDoctor/saveDoctor")
	public String saveDoctor(@ModelAttribute("doctor") Doctor doctor,
			@RequestParam("fileImage") MultipartFile multipartFile) throws IOException {

		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		doctor.setImage(fileName);

		Doctor savedDoctor = doctorService.saveDoctor(doctor);

		String uploadDir = "./doctor-image/" + savedDoctor.getId();

		Path uploadPath = Paths.get(uploadDir);

		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		try (InputStream inputStream = multipartFile.getInputStream()) {

			Path filePath = uploadPath.resolve(fileName);
			System.out.print(filePath.toFile().getAbsolutePath());
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

		} catch (IOException e) {
			throw new IOException("Could not save uploaded file" + fileName);
		}

		return "redirect:/ViewDoctor";
	}

	@PostMapping("/ViewDoctor/updateDoctor")
	public String updateDoctor(@ModelAttribute("doctor") Doctor doctor,
			@RequestParam("fileImage") MultipartFile multipartFile) throws IOException {

		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		if (!fileName.equals("")) {
			doctor.setImage(fileName);

			Doctor savedDoctor = doctorService.saveDoctor(doctor);

			String uploadDir = "./doctor-image/" + savedDoctor.getId();

			Path uploadPath = Paths.get(uploadDir);

			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			try (InputStream inputStream = multipartFile.getInputStream()) {

				Path filePath = uploadPath.resolve(fileName);
				System.out.print(filePath.toFile().getAbsolutePath());
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

			} catch (IOException e) {
				throw new IOException("Could not save uploaded file" + fileName);
			}

			return "redirect:/ViewDoctor";
		}

		else {
			doctorService.saveDoctor(doctor);
			return "redirect:/ViewDoctor";
		}
	}

	@GetMapping("/ViewDoctor/deleteDoctor/{id}")
	public String deletDoctor(@PathVariable(value = "id") int id, Model model) {
		this.doctorService.deleteDoctorbyID(id);
		return "redirect:/ViewDoctor";
	}

	@RequestMapping("/showModalForUpdate")
	@ResponseBody
	public Doctor updateDoctor(int id) {
		return doctorService.getDoctorById(id);

	}

	@GetMapping("/addDoctorForm")
	public String dispalyAddDoctorForm() {
		return "admin_AddDoctorForm";
	}

	@GetMapping("/updateDoctorForm")
	public String dispalyUpdateDoctorForm(int id, Model model) {
		Doctor doctor = doctorService.getDoctorById(id);
		model.addAttribute("doctorId", doctor.getId());
		model.addAttribute("doctorName", doctor.getName());
		model.addAttribute("doctorEmail", doctor.getEmail());
		model.addAttribute("doctorTelephone", doctor.getTelephone());
		model.addAttribute("doctorImagePath", doctor.getImage());
		model.addAttribute("doctorSpecialty", doctor.getSpecialty());
		model.addAttribute("doctorPassword", doctor.getPassword());
		return "admin_UpdateDoctorForm";
	}

	@RequestMapping("/processLoginAdmin")
	public String loginProcess(@RequestParam("username") String username, @RequestParam("password") String password) {
		User user = userservice.getUserByusername(username);
		String n1;
		if (username.equals(user.getUsername())) {
			if (password.equals(user.getPassword())) {
				uid = user.getId();
				n1 = "redirect:/AdminPanelHomeForm";
			} else {
				n1 = "redirect:/AdminLogout?InvalidPass";
			}
		} else {
			n1 = "redirect:/AdminLogout?InvalidEmail";
		}
		return n1;

	}

	@GetMapping("/ViewReportForm")
	public String displayReportForm(Model model) {
		model.addAttribute("listdoctor",doctorService.getAllDoctors());
		return "admin_Reports";
	}

	@GetMapping("/exportDoctorsPDF")
	public void exportDoctorPDF(HttpServletResponse response, @RequestParam("specialty") String specialty)
			throws DocumentException, IOException {

		List<Doctor> listDoctor;

		response.setContentType("application/pdf");

		LocalTime localtime = LocalTime.now();
		Time time = Time.valueOf(localtime);

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		String Date = dateFormatter.format(new Date());

		String currentDateTime = Date + time;

		System.out.println(currentDateTime);

		String headerKey = "Content-Disposition";
		String headerValue = "inline; filename=doctor" + currentDateTime + ".pdf";

		response.setHeader(headerKey, headerValue);

		if (specialty.equals("All")) {
			listDoctor = doctorService.getAllDoctors();
		} else {
			listDoctor = doctorService.getAllDoctorsBySpecialty(specialty);
		}

		DoctorPDFExporter exporter = new DoctorPDFExporter(listDoctor);
		exporter.export(response);

		System.out.println(currentDateTime);
	}

	@GetMapping("/exportPatientPDF")
	public void exportPatientPDF(HttpServletResponse response) throws DocumentException, IOException {

		List<Patient> listPatient;

		response.setContentType("application/pdf");

		LocalTime localtime = LocalTime.now();
		Time time = Time.valueOf(localtime);

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		String Date = dateFormatter.format(new Date());

		String currentDateTime = Date + time;

		System.out.println(currentDateTime);

		String headerKey = "Content-Disposition";
		String headerValue = "inline; filename=patient" + currentDateTime + ".pdf";

		response.setHeader(headerKey, headerValue);

		listPatient = patienteservice.getAllPatient();

		PatientPDFExporter exporter = new PatientPDFExporter(listPatient);
		exporter.export(response);

		System.out.println(currentDateTime);
	}
	
	
	@GetMapping("/exportActiveSchedulePDF")
	public void exportActiveSchedulePDF(HttpServletResponse response) throws DocumentException, IOException {
		List<Schedule> listSchedule;

		LocalDate localdate = LocalDate.now();
		java.sql.Date date1 = java.sql.Date.valueOf(localdate);
		
		response.setContentType("application/pdf");

		LocalTime localtime = LocalTime.now();
		Time time = Time.valueOf(localtime);

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		String Date = dateFormatter.format(new Date());

		String currentDateTime = Date + time;

		System.out.println(currentDateTime);

		String headerKey = "Content-Disposition";
		String headerValue = "inline; filename=doctor" + currentDateTime + ".pdf";

		response.setHeader(headerKey, headerValue);
		
		listSchedule = scheduleservice.getActiveSchedules(date1);

		ActiveShedulePDFExporter exporter = new ActiveShedulePDFExporter(listSchedule);
		exporter.export(response);

		System.out.println(currentDateTime);
	}

	@GetMapping("/exportInactiveSchedulePDF")
	public void exportInactiveSchedulePDF(HttpServletResponse response) throws DocumentException, IOException {
		List<Schedule> listSchedule;

		LocalDate localdate = LocalDate.now();
		java.sql.Date date1 = java.sql.Date.valueOf(localdate);
		
		response.setContentType("application/pdf");

		LocalTime localtime = LocalTime.now();
		Time time = Time.valueOf(localtime);

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		String Date = dateFormatter.format(new Date());

		String currentDateTime = Date + time;

		System.out.println(currentDateTime);

		String headerKey = "Content-Disposition";
		String headerValue = "inline; filename=doctor" + currentDateTime + ".pdf";

		response.setHeader(headerKey, headerValue);
		
		listSchedule = scheduleservice.getInactiveSchedules(date1);

		ActiveShedulePDFExporter exporter = new ActiveShedulePDFExporter(listSchedule);
		exporter.export(response);

		System.out.println(currentDateTime);
	}
}
