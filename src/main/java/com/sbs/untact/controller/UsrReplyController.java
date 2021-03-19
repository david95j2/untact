package com.sbs.untact.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.untact.dto.Article;
import com.sbs.untact.dto.Reply;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.service.ArticleService;
import com.sbs.untact.service.ReplyService;
import com.sbs.untact.util.Util;

@Controller
public class UsrReplyController {
	
	@Autowired
	private ReplyService replyService;
	@Autowired
	private ArticleService articleService;
	
	@RequestMapping("/usr/reply/doAddReply")
	@ResponseBody
	public ResultData doAddReply(@RequestParam Map<String, Object> param, HttpSession session) {
		int loginedMemberId = Util.getAsInt(session.getAttribute("loginedMemberId"), 0);

		if (param.get("body") == null) {
			return new ResultData("F-1", "내용을 입력하세요");
		}
		
		if (param.get("articleId") == null) {
			return new ResultData("F-1", "게시물 번호를 입력해주세요.");
		}

		param.put("memberId", loginedMemberId);

		return replyService.addReply(param);
	}
	
	@RequestMapping("/usr/reply/list")
	@ResponseBody
	public ResultData showList(String relTypeCode, Integer relId) {
		
		if (relTypeCode == null) {
			return new ResultData("F-1", "relTypeCode를 입력해주세요.");
		}
		
		if (relId == null) {
			return new ResultData("F-1", "relId를 입력해주세요.");
		}
		
		if (relTypeCode.equals("article")) {
			Article article = articleService.getArticle(relId);
			
			if (article == null) {
				return new ResultData("F-1", "존재하지 않는 게시글입니다.");
			}
		}
		
		// 한 페이지 내 댓글 갯수 설정변수
		int itemsInAPage = 20;
		
		List<Reply> replies = replyService.getForPrintReplies(relTypeCode,relId);
		
		return new ResultData("S-1", "성공", "replies",replies);
	}
}
