package com.cos.blog.model;

import java.util.List;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.repository.UserRepository;

// RestController는 데이터를 반환하는 형태
@RestController
public class DummyControllerTest {
	
	@Autowired // 의존성주입(DI)
	private UserRepository userRepository;
	
	// http://localhost:8000/blog/dummy/join(요청)
	// 변수로 받으면 http의 body에 변수명과 동일한 정보가 존재하면 자동으로 확인 가능
	// public String join(String username, String password, String email)
	// 객체로 받으면 http의 body에 객체변수명과 동일한 정보가 존재하면 자동으로 확인 가능
	@PostMapping("/dummy/join")
	public String join(User user)
	{
		System.out.println("username : " + user.getUsername());
		System.out.println("password : " + user.getPassword());
		System.out.println("email : " + user.getEmail());
		user.setRole(RoleType.USER);
		userRepository.save(user);
		
		return "회원가입이 완료되었습니다.";
	}

	// 변수에 RequestBody 어노테이션을 붙이면 json형태로 받고
	//                  어노테이션을 붙이지 않으면 폼태그 형태로 받음
	// 즉 json 데이터를 받았는데 자바객체로 변환하여 입력받음
	// http://localhost:8000/blog/dummy/user/3
	// Dirty 체크를 해줘서 입력값이 없는 경우 SELECT로 원래 있는 값을 채워주는 어노테이션
	// Repository의 동작 없이 객체가 수정이 될 경우 자동으로 변경됨.
	// Transactional 어노테이션을 붙이면 함수 시작시 Transaction을 시작해서 함수 종료시 commit을 수행함.
	// 조회된 User가 영속성 context에 올라왔으므로 해당 User가 변경될 때 변경된 값만 수정함.
	@Transactional 
	@PutMapping("/dummy/user/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User requestUser)
	{
		System.out.println("id : " + id);
		System.out.println("pwd : " + requestUser.getPassword());
		System.out.println("email : " + requestUser.getEmail());
		
		User user = userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("수정에 실패하였습니다."));
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		
		//save는 ID가 없거나 존재하지 않는 경우 INSERT
		//save는 ID가 있으면 UPDATE(들어가 있지 않은 정보는 NULL로 수정된다)
		//userRepository.save(user);
		
		return user;
	}
	
	@DeleteMapping("/dummy/user/{id}")
	public String delete(@PathVariable int id)
	{
		try
		{
			userRepository.deleteById(id);
		}
		catch ( EmptyResultDataAccessException e )
		{
			return "삭제에 실패했습니다. id : " + id;
		}
		
		return "삭제되었습니다. id : " + id;
	}

	// {id} 주소로 파라미터를 입력받을수 있음
	// @PathVariable 어노테이션을 붙이고 변수 타입과 변수명은 괄호 안에 표시된 것과 동일해야 함.
	// http://localhost:8000/blog/dummy/user/3
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id)
	{
		// findById는 Optional로 감싸져있는데 get()을 사용하면 프로그래머가 NULL이 아님을 보장해야 함.
		// User user = userRepository.findById(id).get(); 
		/*
		 * orElseGet을 사용하면 Supplier로 감싸져있는 파라미터가 필요하여 get()함수를 구현해야 함.
		 * 조회하려는 ID값이 존재하면 있는 것을 리턴하고 없으면 빈 객체 생성 후 리턴
		 * User user = userRepository.findById(id).orElseGet( new Supplier<User>() {
		 * 
		 * @Override public User get() { // TODO Auto-generated method stub return new
		 * User(); } } );
		 */
		// orElseThrow를 사용하면 Supplier로 예외를 리턴할 수 있음
		// 조회하려는 ID값이 존재하면 있는 것을 리턴하고 없으면 IllegalArgumentException을 지정하여 오류 종류 표시
		/*
		 * 풀어쓴 형태
		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
			@Override
			public IllegalArgumentException get() {
				// TODO Auto-generated method stub
				return new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. id : " + id);
			}
		});
		 */
		// 람다식
		User user = userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. id : " + id));

		// 만약 Controller 어노테이션을 사용했다면 웹페이지에서 객체 해석이 불가하겠지만
		// RestController 어노테이션을 사용하였으므로 웹브라우져가 이용할 수 있는 언어(json)으로 변환
		// 스프링에서는 json으로 변환하려면 Gson라이브러리 등을 이용하여 변환해야했지만
		// 스프링부트에서는 HttpMesaageConveter가 자동으로 작동하여 jSon으로 변경하여 브라우져에 json을 던져준다.
		return user;
	}
	
	@GetMapping("/dummy/user")
	public List<User> list()
	{
		return userRepository.findAll();
	}
	
	@GetMapping("/dummy/user/page")
	public List<User> pageList(@PageableDefault(size = 2, sort = "id", direction = Sort.Direction.DESC) Pageable pageable)
	{
		Page<User> pageUser = userRepository.findAll(pageable);
		// Page안에 들어있는 값들을 함수로 확인할 수 있다.
		// ex) pageUser.isLast() 최종 페이지 여부 
		return pageUser.getContent();
	}
}
