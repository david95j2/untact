package com.sbs.untact.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.untact.dto.Member;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.service.MemberService;

@Controller
public class AdmMemberController {

	@Autowired
	private MemberService memberService;
	
	@RequestMapping("/adm/member/login")
	public String login() {
		return "adm/member/login";
	}
	
	@RequestMapping("/adm/member/doLogin")
	@ResponseBody
	public ResultData doLogin(String loginId, String loginPw, HttpSession session) {
		if (loginId == null) {
			return new ResultData("F-1", "아이디를 입력해주세요.");
		}

		Member memberExisted = memberService.getMemberByLoginId(loginId);

		if (memberExisted == null) {
			return new ResultData("F-2", "존재하지 않는 아이디입니다.");
		}

		if (memberExisted.getLoginPw() == null) {
			return new ResultData("F-1", "비밀번호를 입력해주세요.");
		}

		if (memberExisted.getLoginPw().equals(loginPw) == false) {
			return new ResultData("F-3", "비밀빈호가 일치하지 않습니다.");
		}
		
		if (memberService.isAdmin(memberExisted) == false) {
			return new ResultData("F-3", "관리자만 이용 가능합니다.");
		}

		session.setAttribute("loginedMemberId", memberExisted.getId());

		return new ResultData("S-1", String.format("%s님 환영합니다.", memberExisted.getNickname()));

	}

	@RequestMapping("/adm/member/doModify")
	@ResponseBody
	public ResultData doModify(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		if (param.isEmpty()) {
			return new ResultData("S-2", "정보수정을 취소합니다.");
		}
		
		int loginedMemberId = (int) req.getAttribute("loginedMemberId");
		param.put("id", loginedMemberId);
		
		return memberService.modifyMemberInfoById(param);
	}
}
