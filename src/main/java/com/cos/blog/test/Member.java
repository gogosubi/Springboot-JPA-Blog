package com.cos.blog.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

// @Getter Getter 함수만 생성
// @Setter Setter 함수만 생성
@Data // Getter & Setter 함수 생성
// @AllArgsConstructor // 모든 변수가 들어있는 생성자
// @RequiredArgsConstructor // final이 있는 변수만 들어있는 생성자
@NoArgsConstructor // 변수가 없는 Default 생성자
public class Member {
	private int id;
	private String username;
	private String password;
	private String email;
	
	@Builder
	public Member(int id, String username, String password, String email) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
	}
}
