package com.cos.blog.controller.api;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.dto.ReplySaveRequestDto;
import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.Board;
import com.cos.blog.model.Reply;
import com.cos.blog.service.BoardService;

@RestController
public class BoardApiController 
{	
	@Autowired
	private BoardService boardService;
	
	@PostMapping("/api/board")
	public ResponseDto<Integer> save(@RequestBody Board board, @AuthenticationPrincipal PrincipalDetail principal)
	{
		System.out.println("BoardApiController : save 호출됨");
		boardService.글쓰기(board, principal.getUser());
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@DeleteMapping("/api/board/{id}")
	public ResponseDto<Integer> deleteById(@PathVariable int id)
	{
		System.out.println("BoardApiController : delete 호출됨");
		boardService.삭제하기(id);

		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PutMapping("/api/board/{id}")
	public ResponseDto<Integer> update(@PathVariable int id, @RequestBody Board board)
	{
		
		boardService.글수정하기(id, board);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);		
	}	
	
	/*
	 * 전통적인 방식의 로그인 방법(삭제)
	 * spring security를 이용하면서 주소도 변경함.
	@PostMapping("/api/user/login")
	public ResponseDto<Integer> login(@RequestBody User user, HttpSession httpSession) // HttpSession을 변수로 입력 받는 방법
	//public ResponseDto<Integer> login(@RequestBody User user) // HttpSession을 DI하는 방법
	{
		System.out.println("UserApiController : login 호출됨");
		User principal = userService.로그인(user);
		
		if ( principal != null)
		{
			System.out.println("USERSERVICE : " + principal.getUsername());
			httpSession.setAttribute("principal", principal);
		}
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	*/

	
	@PostMapping("/api/board/{boardId}/reply")
	public ResponseDto<Integer> replySave(@RequestBody ReplySaveRequestDto replySaveRequestDto)
	{
		boardService.댓글쓰기(replySaveRequestDto);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);		
	}
	/** 
	 * 댓글 쓰기에 너무 많은 입력 변수를 받으므로 ReplySaveRequestDto를 만들어 한 번에 넘겨주는 방식으로 처리함.
	public ResponseDto<Integer> replySave(@PathVariable int boardId, @RequestBody Reply reply, @AuthenticationPrincipal PrincipalDetail principal)
	{
		System.out.println("BoardApiController : replySave 호출됨 => " + reply.getContent());
		boardService.댓글쓰기(principal.getUser(), boardId, reply);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}	
	 */
	
	@DeleteMapping("/api/board/{boardId}/reply/{replyId}")
	public ResponseDto<Integer> replyDelete(@PathVariable int replyId)
	{
		System.out.println("BoardApiController : replyDelete 호출됨 => " + replyId);
		boardService.댓글삭제하기(replyId);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
}
