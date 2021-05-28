package com.cos.blog.controller;

import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.cos.blog.model.KaKaoProfile;
import com.cos.blog.model.OAuthToken;
import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// spring security를 이용하여
// 인증이 안된 사용자들이 출입할 수 있는 경로를 /auth/ 하위 디렉토리 허용
// 그냥 주소가 / 이면 index.jsp 허용
// static 이하에 있는 /js/, /css/, /image/ 하위 디렉토리 허용
@Controller
public class UserController {
	
	@Value("${kakao.key}")
	private String kakaoKey; 
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@GetMapping("/auth/joinForm")
	public String joinForm()
	{
		return "user/joinForm";
	}
	
	@GetMapping("/auth/loginForm")
	public String loginForm()
	{
		return "user/loginForm";
	}
	
	@GetMapping("/user/updateForm")
	public String updateForm()
	{
		return "user/updateForm";
	}
	
	@GetMapping("/auth/kakao/callback")
	public String kakaoCallback(String code)
	{
		// 카카오 로그인 한 사용자 정보를 얻기 위하여 
		// POST방식으로 카카오쪽에 요청해야 한다.
		// 요청할 수 있는 템플릿(과거 : HttpsURLConnection, 안드로이드 : Restrofit2)
		RestTemplate rt = new RestTemplate();
		
		// Header 설정
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		// Body설정
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "0eb4722c771f7faca22eeb0910771009");
		params.add("redirect_uri", "http://localhost:8000/auth/kakao/callback");
		params.add("code", code);
		
		// Header와 Body를 하나의 Object에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
		
		// Http POST방식으로  요청하고 response변수 응답받기 
		ResponseEntity<String> response = rt.exchange("https://kauth.kakao.com/oauth/token"
											, HttpMethod.POST
											, kakaoTokenRequest
											, String.class);
		
		// Jason데이터를 담을수 있는 Object 생성(Gson, Json Simple, ObjectMapper)
		ObjectMapper objectMapper = new ObjectMapper();
		
		// Response에서 우리가 사용할 수 있는 객체로 담기
		OAuthToken oAuthToken = null;
		try 
		{
			oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} 
		catch (JsonMappingException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (JsonProcessingException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("카카오엑세스 토큰 : " + oAuthToken.getAccess_token());
		

		// 카카오 로그인 한 사용자 정보를 얻기 위하여 
		// POST방식으로 카카오쪽에 요청해야 한다.
		// 요청할 수 있는 템플릿(과거 : HttpsURLConnection, 안드로이드 : Restrofit2)
		RestTemplate rt2 = new RestTemplate();
		
		// Header 설정
		HttpHeaders headers2 = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		headers.add("Authorization", "Bearer " + oAuthToken.getAccess_token());
		
		// Header와 Body를 하나의 Object에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);
		
		// Http POST방식으로  요청하고 response변수 응답받기 
		ResponseEntity<String> response2 = rt2.exchange("https://kapi.kakao.com/v2/user/me"
											, HttpMethod.POST
											, kakaoProfileRequest
											, String.class);
		
		System.out.println(response2.getBody());
		

		
		// Jason데이터를 담을수 있는 Object 생성(Gson, Json Simple, ObjectMapper)
		ObjectMapper objectMapper2 = new ObjectMapper();
		
		// Response에서 우리가 사용할 수 있는 객체로 담기
		KaKaoProfile kakaoProfile = null;
		try 
		{
			kakaoProfile = objectMapper2.readValue(response2.getBody(), KaKaoProfile.class);
		} 
		catch (JsonMappingException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (JsonProcessingException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// User 오브젝트 : username, password, email
		System.out.println("KaKaoProfile ID : " + kakaoProfile.getId());
		System.out.println("KaKaoProfile Email : " + kakaoProfile.kakao_account.getEmail());
		
		System.out.println("blog ID : " + kakaoProfile.kakao_account.getEmail() + "_" + kakaoProfile.getId());
		System.out.println("blog email: " + kakaoProfile.kakao_account.getEmail());
		
		User kakaoUser = null;
		
		String kakaoUserName = kakaoProfile.kakao_account.getEmail() + "_" + kakaoProfile.getId();
		
		User originUser = userService.회원찾기(kakaoUserName);		
		if ( originUser.getUsername() == null )
		{
			kakaoUser = User.builder()
							.username(kakaoUserName)
							.password(kakaoKey)
							.email(kakaoProfile.kakao_account.getEmail())
							.role(RoleType.USER)
							.oauth("kakao")
							.build();
			
			// 회원가입자 or 비가입자 체크해서 처리		
			userService.회원가입(kakaoUser);
			
			System.out.println("회원가입완료");
		}
		else
		{	
			System.out.println("기존회원 로그인");
		}
		
		// 로그인 Session 처리
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUserName, kakaoKey));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return "redirect:/";
	}
}
