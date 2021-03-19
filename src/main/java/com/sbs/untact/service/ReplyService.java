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
}
