package com.cos.blog.controller.api;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;

@RestController
public class UserApiController 
{
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
//	@Autowired
//	private HttpSession httpSession;
	
	@PostMapping("/auth/joinProc")
	public ResponseDto<Integer> save(@RequestBody User user)
	{
		System.out.println("UserApiController : save 호출됨");
		userService.회원가입(user);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PutMapping("/user")
	public ResponseDto<Integer> update(@RequestBody User user)
	/* Security Context 구현시 필요했던 입력 파라미터
	public ResponseDto<Integer> update(@RequestBody User user
										, @AuthenticationPrincipal PrincipalDetail principalDetail
										, HttpSession session)
	*/
	{
		System.out.println("UserApiController : update 호출 완료");
		userService.회원수정(user);
		
		// 여기서는 트랜잭션이 종료되기 때문에 DB 값은 변경되었음
		// 하지만, 세션값은 변경되지 않은 상태이기 때문에 직접 세션값을 변경해 줄 것임.
		// 따라서 Authentication을 직접 세션에 넣어줘야 한다.
		
		
//		// Spring Security 구조
//		// 사용자에게 로그인 요청이오면 인증필터가 AuthenticationToken에 저장하고 AuthenticationManager에 던져준다.
//		// AuthenticationManger는 각 AuthenticationProvier를 만들어서 UserDetailService를 만들고 User가 존재하는지 여부를 판단한다.(아이디만 판단함. 비밀번호는 Manager에서 알아서 관리함)
//		// 비밀번호를 확인해서 SEcurityContextHolder에 던져준다.
//		// SecurityContext 홀더는 SecurityContext를 가지고 있고 SecurityContext는 Authentication을 포함하고 있다.
//		// 아래 코딩으로 구현된 방법은 Authentication을 만들어서 Session에 직접 넣어주는 방식을 구현했는데 Spring에서 막아뒀다고 한다.
//		Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetail, null);
//		
//		SecurityContext securityContext = SecurityContextHolder.getContext();
//		securityContext.setAuthentication(authentication);
//		
//		// session에 저장
//		session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
		
		// 변경된 DB와 변경된 비밀번호를 가지고 Session에 입력한다.
		// UsernamePasswordAuthenticationToken은 비밀번호를 암호화 할 수 있으므로 암호화 하지 않고 바로 넣어주면 됨.
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
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
