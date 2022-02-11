package com.project.demo.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.demo.Utility;
import com.project.demo.model.Patient;
import com.project.demo.service.PatientService;

@Controller
public class PatientLoginController {

	String email = null, telephone = null, name = null, address = null;
	int pid;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private PatientService patientservice;

	@GetMapping("/PatientRegistration")
	public String displayRegistrationForm(Model model) {

		model.addAttribute("email", email);
		model.addAttribute("telephone", telephone);
		model.addAttribute("name", name);
		model.addAttribute("address", address);
		return "patient_Registration";
	}

	@GetMapping("/PatientLogIn")
	public String displayLoginForm() {
		
		this.email = null;
		this.name = null;
		this.telephone = null;
		this.address = null;

		return "patient_LogIn";
	}

	@GetMapping("/PatientResetPassword")
	public String displayResetPasswordForm() {

		return "patient_LogIn_ResetPass";
	}

	

	@RequestMapping("/processPasswordRest")
	public String passwordResetProcess(@RequestParam("email") String email, HttpServletRequest request)
			throws UnsupportedEncodingException, MessagingException {

		Patient patient1 = patientservice.getPatientByemail(email);

		if (!email.equals(patient1.getEmail())) {
			return "redirect:/PatientResetPassword?InvalidEmail";
		} else {

			String resetPasswordLink = Utility.getSiteURL(request) + "/patient_ResetPassword?id=" + patient1.getId();

			System.out.println(resetPasswordLink);

			senEmail(email, resetPasswordLink);

			return "redirect:/PatientResetPassword?Success";
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
	
	@GetMapping("/patient_ResetPassword")
	public String displayResetPasswordForm2(@RequestParam("id")int id, Model model) {

		model.addAttribute("patientid",id);
		return "patient_ResetPassword";
	}

	@RequestMapping("/processPasswordChange")
	public String passwordChangeProcess(@RequestParam("id") int id,@RequestParam("password") String password,@RequestParam("confirmpassword") String confirmpassword) {
		Patient patient = patientservice.getPatientById(id);
		if(!password.equals(confirmpassword)) {
			return "redirect:/patient_ResetPassword?InvalidPass";
		}
		else {
			patient.setPassword(password);
			patientservice.savePatient(patient);
			return "patient_ResetPasswordSuccessfull";
		}
		
	}

	@PostMapping("/processRegistration")
	public String registrationProcess(@RequestParam("email") String email,
			@RequestParam("confirmpassword") String conpass, @ModelAttribute("patient") Patient patient) {
		this.email = null;
		this.name = null;
		this.telephone = null;
		this.address = null;
		System.out.println(this.email);

		Patient patient1 = patientservice.getPatientByemail(email);
		if (email.equals(patient1.getEmail())) {

			this.email = patient.getEmail();
			this.name = patient.getName();
			this.telephone = patient.getTelephone();
			this.address = patient.getAddress();

			return "redirect:/PatientRegistration?FailEmail";
		} else if (!conpass.equals(patient.getPassword())) {

			this.email = patient.getEmail();
			this.name = patient.getName();
			this.telephone = patient.getTelephone();
			this.address = patient.getAddress();

			return "redirect:/PatientRegistration?FailPass";
		} else {
			this.email = patient.getEmail();
			patientservice.savePatient(patient);
			return "redirect:/PatientLogIn?Success";
		}

	}

	@RequestMapping("/processLogin")
	public String loginProcess(@RequestParam("email") String email, @RequestParam("password") String password) {
		Patient patient = patientservice.getPatientByemail(email);
		String n1;
		if (email.equals(patient.getEmail())) {
			if (password.equals(patient.getPassword())) {
				pid = patient.getId();
				n1 = "patient_Home";
			} else {
				n1 = "redirect:/PatientLogIn?InvalidPass";
			}
		} else {
			n1 = "redirect:/PatientLogIn?InvalidEmail";
		}
		return n1;

	}
}
