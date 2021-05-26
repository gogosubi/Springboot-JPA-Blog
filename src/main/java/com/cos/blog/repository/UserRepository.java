package com.cos.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.blog.model.User;

// 자동으로 Bean이 등록됨. -> JpaRepository가 @Repository 어노테이션을 가지고 있음.
public interface UserRepository extends JpaRepository<User, Integer> {
	// SELECT * FROM user where username = ?
	Optional<User> findByUsername(String username);
	/*
	 * 전통적인 방식의 로그인 방법(삭제)
	 * spring security를 이용하면서 로직 삭제
	// JPA Naming 전략
	// SELECT * FROM USER WHERE username = ? AND password = ?;
	User findByUsernameAndPassword(String username, String password);
	@Query(value="SELECT * FROM USER WHERE username = ? AND password = ?", nativeQuery = true)
	User login(String username, String password);
	 */
}
