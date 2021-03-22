package com.sbs.untact.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.untact.dao.ReplyDao;
import com.sbs.untact.dto.Reply;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.util.Util;

@Service
public class ReplyService {
	
	@Autowired
	MemberService memberService;
	@Autowired
	ReplyDao replyDao;
	
	// 댓글 작성
	public ResultData addReply(Map<String, Object> param) {
		replyDao.addReply(param);

		// 추가된 게시물 id 받기
		int id = Util.getAsInt(param.get("id"), 0);

		return new ResultData("S-1", "해당 게시물이 등록되었습니다.", "id", id);
	}
	
	// relTyCode 범주안에 relId에 관한 목록 리스팅
	public List<Reply> getForPrintReplies(String relTypeCode, Integer relId) {
		return replyDao.getForPrintReplies(relTypeCode,relId);
	}

	// id 인 댓글 받기
	public Reply getReply(int id) {
		return replyDao.getReply(id);
	}
	
	// 댓글 삭제 권한 체크
	public ResultData getActorCanDeleteRd(Reply reply, int actorId) {
		if (reply.getMemberId() == actorId) {
			return new ResultData("S-1", "가능합니다.");
		}

		if (memberService.isAdmin(actorId)) {
			return new ResultData("S-2", "가능합니다.");
		}

		return new ResultData("F-1", "권한이 없습니다.");	}
	
	// 댓글 삭제
	public ResultData deleteReply(int id) {
		replyDao.deleteReply(id);
		
		return new ResultData("S-1", "해당 댓글이 삭제되었습니다.");
	}
}
