package com.sbs.untact.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.untact.dao.ArticleDao;
import com.sbs.untact.dto.Article;
import com.sbs.untact.dto.ResultData;

@Service
public class ArticleService {

	@Autowired
	private ArticleDao articleDao;

	// 해당 id 게시물 가져오기
	public Article getArticle(int id) {
		return articleDao.getArticle(id);
	}

	// 게시물 추가하기
	public ResultData addArticle(String title, String body) {
		int id = articleDao.addArticle(title, body);
		
		return new ResultData("S-1", "해당 게시물이 등록되었습니다.", "id", id);
	}

	// 게시물 삭제하기
	public ResultData deleteArticle(int id) {
		Boolean rs = articleDao.deleteArticle(id);
		
		if (rs == false) {
			return new ResultData("S-1", "해당 게시물이 삭제되었습니다.");
		}
		
		return new ResultData("S-1", "해당 게시물이 삭제되었습니다.");
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
}
