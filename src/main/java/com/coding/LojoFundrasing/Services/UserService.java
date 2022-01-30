package com.coding.LojoFundrasing.Services;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.coding.LojoFundrasing.Models.User;
import com.coding.LojoFundrasing.Repos.UserRepo;

@Service
public class UserService {
	@Autowired
	private UserRepo urepo;
	
	
	//register user
	public User registerUser(User user) {
		String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
		user.setPassword(hashed);
		return urepo.save(user);
	}
	
	//find user by email
	public User findUserbyEmail(String email) {
		return urepo.findByEmail(email);
	}
	
	//find user by id
	public User findUserbyId(Long id) {
		return urepo.findById(id).orElse(null);
	}
	
	//authenticate user
	public boolean authenticateUser(String email, String password) {
		User user = urepo.findByEmail(email);
		if (user == null){
			return false;
		}
		else {
			if(BCrypt.checkpw(password, user.getPassword())) {
				return true;
			}
			return false;
		}
	}
    public List<User> listAll() {
        return urepo.findAll();
    }
}
