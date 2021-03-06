package com.sbs.untact.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

@Controller
public class UsrReplyController {

	@Autowired
	private ReplyService replyService;
	@Autowired
	private ArticleService articleService;

	@RequestMapping("/usr/reply/doAdd")
	@ResponseBody
	public ResultData doAddReply(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		int loginedMemberId = (int) req.getAttribute("loginedMemberId");

		if (param.get("body") == null) {
			return new ResultData("F-1", "내용을 입력하세요");
		}

		if (param.get("relTypeCode") == null) {
			return new ResultData("F-1", "relyTpeCode를 'article'로 설정해주세요.");
		}

		if (param.get("relId") == null) {
			return new ResultData("F-1", "relId를 설정해주세요.");
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

		List<Reply> replies = replyService.getForPrintReplies(relTypeCode, relId);

		return new ResultData("S-1", "성공", "replies", replies);
	}

	@RequestMapping("/usr/reply/doDelete")
	@ResponseBody
	public ResultData doDelete(Integer id, HttpServletRequest req) {
		int loginedMemberId = (int) req.getAttribute("loginedMemberId");

		if (id == null) {
			return new ResultData("F-1", "Id를 입력해주세요.");
		}

		Reply reply = replyService.getReply(id);

		if (reply == null) {
			return new ResultData("F-2", "해당 댓글이 존재하지 않습니다.");
		}

		ResultData actorCanModifyRd = replyService.getActorCanDeleteRd(reply, loginedMemberId);

		if (actorCanModifyRd.isFail()) {
			return actorCanModifyRd;
		}

		return replyService.deleteReply(id);

	}

	@RequestMapping("/usr/reply/doModify")
	@ResponseBody
	public ResultData doModify(Integer relId, String body, HttpServletRequest req) {
		int loginedMemberId = (int) req.getAttribute("loginedMemberId");

		if (body == null) {
			return new ResultData("F-1", "내용를 입력해주세요.");
		}

		if (relId == null) {
			return new ResultData("F-1", "댓글 번호를 입력해주세요.");
		}

		Reply reply = replyService.getReply(relId);

		if (reply == null) {
			return new ResultData("F-2", "해당 댓글이 존재하지 않습니다.");
		}

		ResultData actorCanModifyRd = replyService.getActorCanModifyRd(reply, loginedMemberId);

		if (actorCanModifyRd.isFail()) {
			return actorCanModifyRd;
		}

		return replyService.modifyReply(relId, body);

	}

}
