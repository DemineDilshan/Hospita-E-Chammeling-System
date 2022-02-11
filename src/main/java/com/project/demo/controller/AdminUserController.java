package com.project.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.demo.model.User;
import com.project.demo.service.UserService;

@Controller
public class AdminUserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/ViewUserForm")
	public String displayUser(Model model) {
		model.addAttribute("listUser", userService.getAllUsers());
		return "admin_User";
	}
	
	@PostMapping("/ViewUserForm/saveUser")
	public String saveUser(@ModelAttribute("user") User user) {
		userService.saveUser(user);
		return "redirect:/ViewUserForm";
	}
	
	@RequestMapping("/ViewUserForm/showModalForUpdate")
	@ResponseBody
	public User updateUser(int id) {
		return userService.getUserById(id);
		
	}
	
	@GetMapping("/ViewUserForm/deleteUser/{id}")
	public String deletDoctor(@PathVariable (value = "id") int id, Model model) {
		this.userService.deleteUserbyID(id);
		return "redirect:/ViewUserForm";
	}
}
