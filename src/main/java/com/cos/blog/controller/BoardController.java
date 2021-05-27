package com.cos.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.cos.blog.service.BoardService;

@Controller
public class BoardController 
{
	@Autowired
	private BoardService boardService;

//  Java Code에서 로그인 확인하기
// Controller에서 세션을 찾는 방법은 AuthenticationPrincipal 에서 확인한다.
//	public String index(@AuthenticationPrincipal PrincipalDetail principal){
//		System.out.println("로그인 사용자 아이디 : " + principal.getUsername());
	@GetMapping({"", "/"})
	public String index(Model model, @PageableDefault(size = 3, sort = "id", direction = Sort.Direction.DESC) Pageable pageable)
	{
		model.addAttribute("boards", boardService.글목록(pageable));
		return "index";
	}
	
	@GetMapping("/board/{id}")
	public String findById(@PathVariable int id, Model model)
	{
		model.addAttribute("board", boardService.글상세보기(id));
		
		return "board/detail";
	}
	
	@GetMapping("/board/saveForm")
	public String saveForm()
	{
		return "board/saveForm";
	}
	
	@GetMapping("/board/{id}/updateForm")
	public String updateForm(@PathVariable int id, Model model)
	{
		System.out.println("UPDATE FORM 함수 호출");
		model.addAttribute("board", boardService.글상세보기(id));
		
		return "board/updateForm";		
	}
}
