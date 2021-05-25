package com.cos.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.blog.model.User;

// 자동으로 Bean이 등록됨. -> JpaRepository가 @Repository 어노테이션을 가지고 있음.
public interface UserRepository extends JpaRepository<User, Integer> {

}
