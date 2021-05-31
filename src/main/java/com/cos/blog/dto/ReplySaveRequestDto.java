package com.cos.blog.dto;

import com.cos.blog.model.Board;
import com.cos.blog.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplySaveRequestDto {
	private int userId;
	private int boardId;
	private String content;
}
