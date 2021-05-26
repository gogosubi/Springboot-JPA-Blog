package com.cos.blog.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.cos.blog.config.auth.PrincipalDetail;

@Controller
public class BoardController {

	@GetMapping({"", "/"})
//  Java Code에서 로그인 확인하기
//	public String index(@AuthenticationPrincipal PrincipalDetail principal){
//		System.out.println("로그인 사용자 아이디 : " + principal.getUsername());
	public String index(){
		return "index";
	}
}
