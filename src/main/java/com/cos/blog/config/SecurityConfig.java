package com.cos.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cos.blog.config.auth.PrincipalDetailService;

// 빈등록 : 스프링 컨테이너에서 객체를 관리할 수 있게 하는 것
@Configuration     // 빈등록(IoC)관리
@EnableWebSecurity // Security 필터 추가 => 스프링 시큐리티가 활성화 되었는데 여기서 관리하겠다.
@EnableGlobalMethodSecurity(prePostEnabled = true) // 특정주소로 접근을 하면 권한 및 인증을 미리 체크하겠다
public class SecurityConfig extends WebSecurityConfigurerAdapter 
{
	@Autowired
	private PrincipalDetailService principalDetailService;
	
	@Bean
	public BCryptPasswordEncoder encodePWD()
	{
		return new BCryptPasswordEncoder();
	}
	
	// 시큐리티가 대신 로그인해주는데 pasword를 가로채기 하는데
	// 해당 password가 뭘로 해쉬가 되어 회원가입이 되었는지 알아야 같은 해쉬로 암호화해서 DB password와 비교할 수 있음
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		// passwordEncoder로 암호화한 친구를 userDetailsService에 들어있는 객체로 넘겨야 함.
		auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable() // csrf 토큰 비활성화(테스트시 걸어두는게 좋음) -> form 태그를 통해 요청이 들어오지 않으면 csrf 토큰이 생성되지 않음.
			.authorizeRequests() // 권한 요청이 오면
				.antMatchers("/", "/auth/**", "/js/**", "/css/**", "/image/**") // 나열된 디렉토리 이하 접근 허용
					.permitAll()         // 전체 허용해주고
				.anyRequest()            // 나머지 URL은
					.authenticated()	 // 인증된 사용자만 가능해야 한다.
			.and()				// 허용되지 않은 경로로 요청이오면
				.formLogin()    			   // 
				.loginPage("/auth/loginForm") // 로그인 페이지로 이동한다.
				.loginProcessingUrl("/auth/loginProc") // 스프링시큐리티가 해당 주소로 요청이 오는 로그인을 가로챈다.
				.defaultSuccessUrl("/") // LOGIN 성공시 이동할 URL
				// .failureUrl("/auth/fail") // LOGIN 실패시 이동할 URL
				;
	}
	

}
