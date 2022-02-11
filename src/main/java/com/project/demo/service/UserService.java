package com.project.demo.service;

import java.util.List;

import com.project.demo.model.User;

public interface UserService {

	List<User> getAllUsers();
	void saveUser(User user);
	User getUserById(int id);
	void deleteUserbyID(int id);
	User getUserByusername(String username);
}
