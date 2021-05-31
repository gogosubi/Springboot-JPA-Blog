package com.cos.blog.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Board {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false, length=100)
	private String title;
	
	@Lob // 대용량 데이터
	private String content; // 섬머노트 라이브러리
	
	private int count; // 조회수
	
	// JPA ManyToMany 확인하는 사이트 : https://ict-nroo.tistory.com/127
	// ManyToOne인 경우 FETCH 타입은 EAGER가 Default.EAGER 자동으로 한 번에 생성됨
	// EAGER는 한 번에 데이터를 가져오는 방식
	// LAZY는 필요할 때 데이터를 가져오는 방식
	@ManyToOne(fetch = FetchType.EAGER) 
	@JoinColumn(name="userId")
	private User user;
		
	// MappedBy가 있으면 연관관계의 주인이 아님. 즉, 다른 테이블에서 FK를 가지고 있음.(컬럼 생성 x)
	// MappedBy시 객체의 변수명을 적는다.
	// OneToMany인 경우 FETCHTYPE은 LAZY가 Default임
	@OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties({"board"}) // 무한참조 방지 : board변수가 호출하는 것은 EAGER 전략을 무시한다. 즉, Jackson 라이브러리 호출하지 않음, 자기 자신이 호출할 때는 EAGER 전략
	@OrderBy("id desc")
	private List<Reply> replys;
	
	@CreationTimestamp
	private Timestamp createDate;
}
