package com.cos.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity // User 클래스가 DB에 테이블로 생성됨.
// @DynamicInsert // NULL값인 경우에는 자동으로 Insert 문에서 제외시켜준다. Default값 이용시 사용.
public class User {

	@Id // Primary Key
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트와 연결된 DB의 넘버링 전략
	private int id; // 시퀀스, auto_increment

	@Column(nullable = false, length = 100, unique = true)
	private String username; // ID

	@Column(nullable = false, length = 100) 
	private String password;
	
	@Column(nullable = false, length = 50)
	private String email;
	
	@Column
	private String oauth;
	
	// @ColumnDefault("'user'") // DB의 Field에 Default값 설정
	// private String role; // Enum을 쓰는게 좋다.
	@Enumerated(EnumType.STRING)
	private RoleType role;
	
	@CreationTimestamp
	private Timestamp createDate;
}
