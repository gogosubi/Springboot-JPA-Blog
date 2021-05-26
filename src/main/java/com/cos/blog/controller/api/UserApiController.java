package com.cos.blog.controller.api;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;

@RestController
public class UserApiController 
{
	@Autowired
	private UserService userService;
	
//	@Autowired
//	private HttpSession httpSession;
	
	@PostMapping("/auth/joinProc")
	public ResponseDto<Integer> save(@RequestBody User user)
	{
		System.out.println("UserApiController : save 호출됨");
		userService.회원가입(user);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/*
	 * 전통적인 방식의 로그인 방법(삭제)
	 * spring security를 이용하면서 주소도 변경함.
	@PostMapping("/api/user/login")
	public ResponseDto<Integer> login(@RequestBody User user, HttpSession httpSession) // HttpSession을 변수로 입력 받는 방법
	//public ResponseDto<Integer> login(@RequestBody User user) // HttpSession을 DI하는 방법
	{
		System.out.println("UserApiController : login 호출됨");
		User principal = userService.로그인(user);
		
		if ( principal != null)
		{
			System.out.println("USERSERVICE : " + principal.getUsername());
			httpSession.setAttribute("principal", principal);
		}
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	*/
	
}
