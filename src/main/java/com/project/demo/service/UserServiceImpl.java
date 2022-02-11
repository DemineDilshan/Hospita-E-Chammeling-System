package com.project.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.demo.model.User;
import com.project.demo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userrepository;
	
	@Override
	public List<User> getAllUsers() {
		return  userrepository.findAll();
	}

	@Override
	public void saveUser(User user) {
		this.userrepository.save(user);
		
	}

	@Override
	public User getUserById(int id) {
		Optional<User> optional = userrepository.findById(id);
		User user = null;
		if (optional.isPresent()) {
			user = optional.get();
		} else {
			throw new RuntimeException("Doctor Not Found for id - " + id);
		}
		return user;
	}

	@Override
	public void deleteUserbyID(int id) {
		this.userrepository.deleteById(id);
		
	}

	@Override
	public User getUserByusername(String username) {
		Optional<User> optional = userrepository.findByusername(username);
		User user = null;
		if (optional.isPresent()) {
			user = optional.get();
		}
		else {
			user = new User();
		}
		return user;
	}

}
