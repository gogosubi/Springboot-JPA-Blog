package com.cos.blog.test;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// 사용자가요청 -> 응답(Html)
// @Controller
// 사용자가 요청 -> 응답(Data)
@RestController
public class HttpControllerTest {
	
	private static final String TAG = "HttpControllerTest : ";
	
	@GetMapping("/http/lombok")
	public String lombokTest()
	{
		// Member m = new Member(1, "syb", "adfa", "aaemail");
		Member m = Member.builder().username("1234").password("fff").email("setoba").build();
		System.out.println(TAG + "getter : " + m.getId());
		m.setId(4000);
		System.out.println(TAG + "getter : " + m.getId());
		
		return "lombok Test";
	}

	// 인터넷브라우져를 통한 요청은 Get방식만 가능하다.
	// http://localhost:8080/http/get (select)
	@GetMapping("/http/get")
	public String getTest(Member m)
	{
		return "get 요청 : " + m.getId() + ", " + m.getUsername() + ", " + m.getPassword() + ", " + m.getEmail();
	}

	// http://localhost:8080/http/post (insert)
	@PostMapping("/http/post")
	// POST, PUT, DELETE 요청시에는 RequestBody 어노테이션을 가지고 입력값을 수신 한다.
	// MIMETYPE에 맞게 MessageConver가 자동으로 변경한다.  
	// MIMETYPE 전체 목록 : https://developer.mozilla.org/ko/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
	// 일반문자열 : test/plain
	// JSON : application/json
	public String postTest(@RequestBody Member m)
	{
		return "post 요청 : " + m.getId() + ", " + m.getUsername() + ", " + m.getPassword() + ", " + m.getEmail();
	}

	// http://localhost:8080/http/put (update)
	@PutMapping("/http/put")
	public String putTest()
	{
		return "put 요청";
	}

	// http://localhost:8080/http/delete (delete)
	@DeleteMapping("/http/delete")
	public String DeleteTest()
	{
		return "delete 요청";
	}
	
}
