package com.cos.blog.controller.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.User;

@RestController
public class UserApiController 
{
	@PostMapping("/api/user")
	public int save(@RequestBody User user)
	{
		System.out.println("UserApiController");
		return 1;
	}
}
