package com.sbs.untact.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.untact.dao.ArticleDao;
import com.sbs.untact.dto.Article;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.util.Util;

@Service
public class ArticleService {

	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private MemberService memberService;

	// 해당 id 게시물 가져오기
	public Article getArticle(int id) {
		return articleDao.getArticle(id);
	}

	// 게시물 추가하기
	public ResultData addArticle(Map<String, Object> param) {
		articleDao.addArticle(param);
		
		// 추가된 게시물 id 받기
		int id = Util.getAsInt(param.get("id"), 0);
		
		return new ResultData("S-1", "해당 게시물이 등록되었습니다.", "id", id);
	}

	// 게시물 삭제하기
	public ResultData deleteArticle(int id) {
		articleDao.deleteArticle(id);
		
		return new ResultData("S-1", "해당 게시물이 삭제되었습니다.","id",id);
	}

	// 게시물 수정
	public ResultData modifyArticle(int id, String title, String body) {
		articleDao.modifyArticle(id, title, body);

		return new ResultData("S-1", "해당 게시물이 수정되었습니다.", "id", id);
	}

	// 게시물 리스팅
	public List<Article> getArticles(String searchKeywordType, String searchKeyword) {
		return articleDao.getArticles(searchKeywordType, searchKeyword);
	}
	
	// 게시물 수정 권한 체크
	public ResultData getActorCanModifyRd(Article article, int actorId) {
		if (article.getMemberId() == actorId) {
			return new ResultData("S-1", "가능합니다.");
		}
		
		if (memberService.isAdmin(actorId)) {
			return new ResultData("S-2", "가능합니다.");
		}
		
		return new ResultData("F-1", "권한이 없습니다.");
	}
	
	// 게시물 삭제 권한 체크
	public ResultData getActorCanDeleteRd(Article article, int actorId) {
		return getActorCanModifyRd(article, actorId);
	}
	
	// detail 시 member의 닉네임 추가로 불러오기
	public Article getForPrintArticle(int id) {
		return articleDao.getForPrintArticle(id);
	}
	
	// list 시 member의 닉네임 추가로 불러오기
	public List<Article> getForPrintArticles(int boardId, String searchKeywordType, String searchKeyword,
			int page, int itemsInAPage) {
		
		int limitStart = (page -1) * itemsInAPage;
		int limitTake = itemsInAPage;
		return articleDao.getForPrintArticles(boardId,searchKeywordType, searchKeyword,limitStart,limitTake);
	}
}
