package com.project.demo.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.LocalDate;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.project.demo.Utility;
import com.project.demo.model.Doctor;
import com.project.demo.model.Patient;
import com.project.demo.service.AppointmentService;
import com.project.demo.service.DoctorService;
import com.project.demo.service.ScheduleService;

@Controller
public class DoctorPageController {

	String currentPassword = null, confirmPassword = null, newPassword = null;

	int did;

	String invalidEmail = null;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private AppointmentService appointmentservice;

	@Autowired
	private DoctorService doctorservice;

	@Autowired
	private ScheduleService scheduleservice;

	@GetMapping("/DoctorLogIn")
	public String DocotrLogin() {
		return "doctor_LogIn";
	}

	@GetMapping("/DoctorLogOut")
	public String DocotrLogOut() {
		return "doctor_LogIn";
	}

	@GetMapping("/ViewDoctorHome")
	public String displayDoctorHome(Model model) {
		System.out.println(did);
		Doctor doctor = doctorservice.getDoctorById(did);
		model.addAttribute("name", doctor.getName());
		model.addAttribute("email", doctor.getEmail());
		model.addAttribute("telephone", doctor.getTelephone());
		model.addAttribute("specialty", doctor.getSpecialty());
		model.addAttribute("image", doctor.getDoctorImagePath());
		return "doctor_Home";
	}

	@GetMapping("/ViewDoctorHomeEdit")
	public String displayDoctorHomeEdit(Model model) {
		System.out.println(did);
		Doctor doctor = doctorservice.getDoctorById(did);
		model.addAttribute("id", doctor.getId());
		model.addAttribute("name", doctor.getName());
		model.addAttribute("email", doctor.getEmail());
		model.addAttribute("telephone", doctor.getTelephone());
		model.addAttribute("specialty", doctor.getSpecialty());
		model.addAttribute("image", doctor.getImage());
		model.addAttribute("password", doctor.getPassword());
		
		return "doctor_HomeEdit";
	}

	@PostMapping("/editDoctor")
	public String updateDoctor(@ModelAttribute("doctor") Doctor doctor,
			@RequestParam("fileImage") MultipartFile multipartFile) throws IOException {



		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		if (!fileName.equals("")) {
			doctor.setImage(fileName);

			Doctor savedDoctor = doctorservice.saveDoctor(doctor);

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

			return "redirect:/ViewDoctorHome";
		}

		else {
			doctorservice.saveDoctor(doctor);
			return "redirect:/ViewDoctorHome";
		}

	
		
		
		
//		Doctor doctor1 = doctorservice.getDcotorByemail(doctor.getEmail());
//
//		if (doctor1.getEmail().equals(doctor.getEmail())) {
//			this.invalidEmail = doctor1.getEmail();
//			return "redirect:/ViewDoctorHomeEdit?InvalidEmail";
//		}
//
//		else {}

	}

	@PostMapping("/editDoctorPassword")
	public String updateDoctorPassword(@RequestParam("currentpassword") String currentPass,
			@RequestParam("newpassword") String newPass, @RequestParam("confirmpassword") String confirmPass) {

		Doctor doctor = doctorservice.getDoctorById(did);

		if (!currentPass.equals(doctor.getPassword())) {
			this.currentPassword = null;
			this.confirmPassword = confirmPass;
			this.newPassword = newPass;
			System.out.println(currentPassword);
			System.out.println(newPassword);
			System.out.println(confirmPassword);
			// return "redirect:/ChangePassword?InvalidCurrentPass";
			return "redirect:/ViewDoctorHomeEdit";

		} else if (!newPass.equals(confirmPass)) {
			this.currentPassword = currentPass;
			this.confirmPassword = null;
			this.newPassword = newPass;
			// return "redirect:/ChangePassword?InvalidConfirmPass";
			return "redirect:/ViewDoctorHomeEdit";

		} else {
			this.currentPassword = null;
			this.confirmPassword = null;
			this.newPassword = null;
			doctor.setPassword(newPass);
			doctorservice.saveDoctor(doctor);
			return "redirect:/ViewDoctorHomeEdit";
		}
	}

	@GetMapping("/ViewDoctorSchedule")
	public String displayDoctorSchedule(Model model) {

		LocalDate localdate = LocalDate.now();
		Date date1 = Date.valueOf(localdate);

		if (!String.valueOf(did).equals(null)) {
			model.addAttribute("listSchedule", scheduleservice.getActiveSchedulesByDoctor(did, date1));
		}

		return "doctor_Schedule";
	}

	@GetMapping("/inactiveSchedulesDoctor")
	public String findInactiveSchedules(Model model) {
		LocalDate localdate = LocalDate.now();
		Date date1 = Date.valueOf(localdate);

		if (!String.valueOf(did).equals(null)) {
			model.addAttribute("listSchedule", scheduleservice.getInactiveSchedulesByDoctor(did, date1));
		}
		return "doctor_Schedule";
	}

	@GetMapping("/ViewDoctorAppointments")
	public String displayDoctorAppointment(Model model, int id) {
		model.addAttribute("listAppointment", appointmentservice.getAllbySchedule(id));

		return "doctor_Appointments";
	}

	@RequestMapping("/processLoginDoctor")
	public String loginProcess(@RequestParam("email") String email, @RequestParam("password") String password) {
		Doctor doctor = doctorservice.getDcotorByemail(email);
		String n1;
		if (email.equals(doctor.getEmail())) {
			if (password.equals(doctor.getPassword())) {
				did = doctor.getId();
				n1 = "redirect:/ViewDoctorHome";
			} else {
				n1 = "redirect:/DoctorLogIn?InvalidPass";
			}
		} else {
			n1 = "redirect:/DoctorLogIn?InvalidEmail";
		}
		return n1;

	}

	@GetMapping("/DoctorResetPassword")
	public String displayDoctorResetPasswordForm() {

		return "doctor_LogIn_ResetPass";
	}

	@RequestMapping("/processDoctorPasswordRest")
	public String doctorPasswordResetProcess(@RequestParam("email") String email, HttpServletRequest request)
			throws UnsupportedEncodingException, MessagingException {

		Doctor doctor = doctorservice.getDcotorByemail(email);

		if (!email.equals(doctor.getEmail())) {
			return "redirect:/DoctorResetPassword?InvalidEmail";
		} else {

			String resetPasswordLink = Utility.getSiteURL(request) + "/dcotorResetPassword?id=" + doctor.getId();

			System.out.println(resetPasswordLink);

			senEmail(email, resetPasswordLink);

			return "redirect:DoctorResetPassword?Success";
		}
	}

	private void senEmail(String email, String resetPasswordLink)
			throws UnsupportedEncodingException, MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("dilshanpromoday10@gmail.com", "Viveka Support");
		helper.setTo(email);

		String subject = "Here's the link for reset your password";

		String content = "<p>Hello,</p>" + "<p>You have requested to reset your password</p>"
				+ "<p>Click the link below to reset your password</p>" + resetPasswordLink
				+ "<p>Ignore this email if you have not made a request</p>";

		helper.setSubject(subject);
		helper.setText(content, true);

		System.out.println("<p><b><a herf=\"" + resetPasswordLink + "\">Change My Password</a></b></p>");

		mailSender.send(message);
	}

	@GetMapping("/dcotorResetPassword")
	public String displayDoctorResetPasswordForm2(@RequestParam("id") int id, Model model) {

		model.addAttribute("patientid", id);
		return "doctor_ResetPassword";
	}

	@RequestMapping("/processDoctorPasswordChange")
	public String doctorPasswordChangeProcess(@RequestParam("id") int id, @RequestParam("password") String password,
			@RequestParam("confirmpassword") String confirmpassword) {
		Doctor doctor = doctorservice.getDoctorById(id);
		if (!password.equals(confirmpassword)) {
			return "redirect:/doctor_ResetPassword?InvalidPass";
		} else {
			doctor.setPassword(password);
			doctorservice.saveDoctor(doctor);
			return "doctor_ResetPasswordSuccessfull";
		}

	}
}
