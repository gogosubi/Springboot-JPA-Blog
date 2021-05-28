package com.cos.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Transactional
	public void 회원가입(User user)
	{
		String encPassword = encoder.encode(user.getPassword());
		user.setPassword(encPassword);
		user.setRole(RoleType.USER);
		
		userRepository.save(user);		
	}
	
	@Transactional
	public void 회원수정(User requestUser)
	{
		System.out.println("회원ID : " + requestUser.getId());
		
		User user = userRepository.findById(requestUser.getId())
								.orElseThrow(()->new IllegalArgumentException("회원찾기실패"));
		
		System.out.println(user.getEmail() + " : " + requestUser.getEmail());
		if ( user.getOauth() == null || user.getOauth().equals(""))
		{
			user.setPassword(encoder.encode(requestUser.getPassword()));
			user.setEmail(requestUser.getEmail());
		}
	}
	
	@Transactional(readOnly = true)
	public User 회원찾기(String username)
	{
		return userRepository.findByUsername(username)
							.orElseGet(()->new User());
	}

	/*
	 * 전통적인 방식의 로그인 방법(삭제)
	 * spring security를 이용하면서 로직 삭제
	@Transactional(readOnly = true) // Select할 때 Transaction 시작, 서비스 종료시 Transaction종료(정합성 보장)
	public User 로그인(User user)
	{
		System.out.println("USERSERVICE : " + user.getUsername());
		return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
	}
	 */

}
