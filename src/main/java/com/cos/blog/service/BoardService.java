package com.cos.blog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.dto.ReplySaveRequestDto;
import com.cos.blog.model.Board;
import com.cos.blog.model.Reply;
import com.cos.blog.model.User;
import com.cos.blog.repository.BoardRepository;
import com.cos.blog.repository.ReplyRepository;
import com.cos.blog.repository.UserRepository;

@Service
public class BoardService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private ReplyRepository replyRepository;
	
	@Transactional
	public void 글쓰기(Board board, User user)
	{
		board.setCount(0);
		board.setUser(user);
		
		boardRepository.save(board);
	}
	
	public Page<Board> 글목록(Pageable pageable)
	{
		return boardRepository.findAll(pageable);
	}
	
	@Transactional(readOnly = true)
	public Board 글상세보기(int id)
	{
		return boardRepository.findById(id)
							.orElseThrow(()->new IllegalArgumentException("글상세보기 실패 : 아이디를 찾을 수 없습니다."));
	}
	
	@Transactional
	public void 삭제하기(int id)
	{
		boardRepository.deleteById(id);		
	}
	
	@Transactional
	public void 글수정하기(int id, Board requestBoard)
	{
		// 수정하기 위해서는 데이터 영속화 필요
		// 즉, 테이블의 입력값을 찾아와서 수정해야 함.
		Board board = boardRepository.findById(id)
				.orElseThrow(()->new IllegalArgumentException("글찾기 실패 : 아이디를 찾을 수 없습니다."));

		board.setTitle(requestBoard.getTitle());
		board.setContent(requestBoard.getContent());
		// 해당 함수 종료시(Service가 종료될 때) 트랜잭션이 종료.
		// 이때 더티체킹을 하여 자동업데이트가 됨 -> db flush		
	}
	
	// 원래 데이터를 받을 때 컨트롤에서 DTO를 만들어서 받는게 좋다.
	@Transactional
	public void 댓글쓰기(ReplySaveRequestDto replySaveRequestDto)
	{		
		/**
		 * 단순 DTO 사용법(네이티브 쿼리보다 이 방식이 더 좋아보임)
		User user = userRepository.findById(replySaveRequestDto.getUserId())
				.orElseThrow(()->new IllegalArgumentException("댓글쓰기 실패 : 유저 id를 찾을 수 없습니다."));
		// 수정하기 위해서는 데이터 영속화 필요
		// 즉, 테이블의 입력값을 찾아와서 수정해야 함.
		Board board = boardRepository.findById(replySaveRequestDto.getBoardId())
				.orElseThrow(()->new IllegalArgumentException("댓글쓰기 실패 : 게시글 id를 찾을 수 없습니다."));

		Reply reply = Reply.builder()
						.user(user)
						.board(board)
						.content(replySaveRequestDto.getContent())
						.build();
		
		replyRepository.save(reply);
		 */
		
		replyRepository.mSave(replySaveRequestDto.getUserId(), replySaveRequestDto.getBoardId(), replySaveRequestDto.getContent());
	}
	/*
	 * DTO 형태로 변경전
	public void 댓글쓰기(User user, int boardId, Reply reply)
	{		
		// 수정하기 위해서는 데이터 영속화 필요
		// 즉, 테이블의 입력값을 찾아와서 수정해야 함.
		Board board = boardRepository.findById(boardId)
				.orElseThrow(()->new IllegalArgumentException("댓글쓰기 실패 : 게시글 id를 찾을 수 없습니다."));

		reply.setUser(user);
		reply.setBoard(board);
		
		replyRepository.save(reply);
	}
	 */
	
	public void 댓글삭제하기(int id)
	{
		replyRepository.deleteById(id);
	}
}
