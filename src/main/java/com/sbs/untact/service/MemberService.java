package com.sbs.untact.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.untact.dao.MemberDao;
import com.sbs.untact.dto.Member;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.util.Util;

@Service
public class MemberService {

	@Autowired
	private MemberDao memberDao;

	// 회원가입
	public ResultData join(Map<String, Object> param) {
		memberDao.addMember(param);

		int id = Util.getAsInt(param.get("id"), 0);

		return new ResultData("S-1", String.format("%s님 환영합니다.", param.get("nickname")), "id", id);
	}

	// 아이디 중복 확인
	public Member getMemberByLoginId(String loginId) {
		return memberDao.getMemberByLoginId(loginId);
	}

	// id 인 회원 가져오기
	public Member getMemberById(int id) {
		return memberDao.getMemberById(id);
	}
	
	// id 인 회원 수정하기
	public ResultData modifyMemberInfoById(Map<String, Object> param) {
		memberDao.modifyMemberInfoById(param);
		
		return new ResultData("S-1", "수정되었습니다.");
	}
}
