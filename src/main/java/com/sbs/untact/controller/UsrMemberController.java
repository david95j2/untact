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
public class UsrMemberController {

	@Autowired
	private MemberService memberService;

	@RequestMapping("/usr/member/doJoin")
	@ResponseBody
	public ResultData doJoin(@RequestParam Map<String, Object> param) {
		if (param.get("loginId") == null) {
			return new ResultData("F-1", "아이디를 입력해주세요");
		}

		Member memberExisted = memberService.getMemberByLoginId((String) param.get("loginId"));

		if (memberExisted != null) {
			return new ResultData("F-2", String.format("%s (은)는 이미 사용중인 아이디입니다.", param.get("loginId")));
		}

		if (param.get("loginPw") == null) {
			return new ResultData("F-1", "비밀번호를 입력해주세요");
		}

		if (param.get("name") == null) {
			return new ResultData("F-1", "이름을 입력해주세요");
		}

		if (param.get("nickname") == null) {
			return new ResultData("F-1", "닉네임을 설정해주세요");
		}

		if (param.get("cellphoneNo") == null) {
			return new ResultData("F-1", "전화번호를 입력해주세요");
		}

		if (param.get("email") == null) {
			return new ResultData("F-1", "이메일을 입력해주세요");
		}

		return memberService.join(param);
	}

	@RequestMapping("/usr/member/doLogin")
	@ResponseBody
	public ResultData doLogin(String loginId, String loginPw, HttpSession session) {
		if (session.getAttribute("loginedMemberId") != null) {
			return new ResultData("F-2", "이미 로그인되어 있습니다.");
		}

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

		session.setAttribute("loginedMemberId", memberExisted.getId());

		return new ResultData("S-1", String.format("%s님 환영합니다.", memberExisted.getNickname()));

	}

	@RequestMapping("/usr/member/doLogout")
	@ResponseBody
	public ResultData doLogout(HttpSession session) {
		if (session.getAttribute("loginedMemberId") == null) {
			return new ResultData("F-2", "이미 로그아웃되어 있습니다.");
		}
		
		session.removeAttribute("loginedMemberId");
		
		return new ResultData("S-1", "로그아웃 되었습니다.");
	}

	@RequestMapping("/usr/member/doModify")
	@ResponseBody
	public ResultData doModify(@RequestParam Map<String, Object> param, HttpSession session) {
		if (session.getAttribute("loginedMemberId") == null) {
			return new ResultData("F-1", "로그인 후 이용해주세요.");
		}
		
		if (param.isEmpty()) {
			return new ResultData("S-2", "정보수정을 취소합니다.");
		}
		
		int loginedMemberId = (int)session.getAttribute("loginedMemberId");
		param.put("id", loginedMemberId);
		
		return memberService.modifyMemberInfoById(param);
	}
}
