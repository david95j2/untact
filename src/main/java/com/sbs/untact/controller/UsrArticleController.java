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
import com.sbs.untact.dto.Board;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.service.ArticleService;
import com.sbs.untact.util.Util;

@Controller
public class UsrArticleController {

	@Autowired
	private ArticleService articleService;

	@RequestMapping("/usr/article/detail")
	@ResponseBody
	public ResultData showDetail(Integer id) {
		if (id == null) {
			return new ResultData("F-1", "Id를 입력해주세요.");
		}
		
		Article article = articleService.getForPrintArticle(id);
		
		if (article == null) {
			return new ResultData("F-2", "존재하지 않는 게시물 번호 입니다.");
		}
		
		return new ResultData("S-1", "불러오기 완료","article",article);

	}

	@RequestMapping("/usr/article/list")
	@ResponseBody
	public ResultData showList(@RequestParam(defaultValue = "1") int boardId, String searchKeywordType,
			String searchKeyword,@RequestParam(defaultValue = "1") int page) {
		
		Board board = articleService.getBoard(boardId);
		
		if (board == null) {
			return new ResultData("F-1", "존재하지 않는 게시판입니다.");
		}
		
		if (searchKeyword == null) {
			searchKeyword = "";
		}

		if (searchKeyword != null && searchKeyword.length() == 0) {
			searchKeyword = "";
		}

		if (searchKeyword != null) {
			searchKeyword = searchKeyword.trim();
		}
		
		// 한 페이지 내 게시물 갯수 설정변수
		int itemsInAPage = 20;
		
		List<Article> articles = articleService.getForPrintArticles(boardId,searchKeywordType, searchKeyword,page,itemsInAPage);
		
		return new ResultData("S-1", "성공", "articles",articles);
	}

	@RequestMapping("/usr/article/doAdd")
	@ResponseBody
	public ResultData doAdd(@RequestParam Map<String, Object> param, HttpSession session) {
		int loginedMemberId = Util.getAsInt(session.getAttribute("loginedMemberId"), 0);

		if (param.get("title") == null) {
			return new ResultData("F-1", "제목을 입력하세요");
		}

		if (param.get("body") == null) {
			return new ResultData("F-1", "내용을 입력하세요");
		}

		param.put("memberId", loginedMemberId);

		return articleService.addArticle(param);
	}

	@RequestMapping("/usr/article/doDelete")
	@ResponseBody
	public ResultData doDelete(Integer id,HttpSession session) {
		int loginedMemberId = Util.getAsInt(session.getAttribute("loginedMemberId"), 0);

		if (id == null) {
			return new ResultData("F-1", "Id를 입력해주세요.");
		}
				
		Article article = articleService.getArticle(id);

		ResultData actorCanModifyRd = articleService.getActorCanDeleteRd(article, loginedMemberId);

		if (actorCanModifyRd.isFail()) {
			return actorCanModifyRd;
		}
		
		if (article == null) {
			return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.");
		}

		return articleService.deleteArticle(id);
	}

	@RequestMapping("/usr/article/doModify")
	@ResponseBody
	public ResultData doModify(Integer id, String title, String body, HttpSession session) {
		int loginedMemberId = Util.getAsInt(session.getAttribute("loginedMemberId"), 0);

		if (id == null) {
			return new ResultData("F-1", "Id를 입력해주세요.");
		}
		if (title == null) {
			return new ResultData("F-1", "제목을 입력해주세요.");
		}
		if (body == null) {
			return new ResultData("F-1", "내용을 입력해주세요.");
		}

		Article article = articleService.getArticle(id);

		if (article == null) {
			return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.");
		}

		ResultData actorCanModifyRd = articleService.getActorCanModifyRd(article, loginedMemberId);

		if (actorCanModifyRd.isFail()) {
			return actorCanModifyRd;
		}

		return articleService.modifyArticle(id, title, body);
	}
}
