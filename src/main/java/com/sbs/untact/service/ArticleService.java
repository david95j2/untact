package com.sbs.untact.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sbs.untact.dto.Article;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.util.Util;

@Service
public class ArticleService {

	private int articlesLastId;
	private List<Article> articles;

	public ArticleService() {
		// 멤버변수 초기화
		articlesLastId = 0;
		articles = new ArrayList<>();

		// 게시물 2개 생성
		articles.add(new Article(++articlesLastId, "2020-12-12 12:12:12", "2020-12-12 12:12:12", "제목1", "내용1"));
		articles.add(new Article(++articlesLastId, "2020-12-12 12:12:12", "2020-12-12 12:12:12", "제목2", "내용2"));

	}
	
	// 해당 id 게시물 가져오기
	public Article getArticle(int id) {
		for (Article article : articles) {
			if (article.getId() == id) {
				return article;
			}
		}
		
		return null;
	}

	// 게시물 리스트 다불러오기
	public List<Article> getArticles(String searchKeyword) {
		if (searchKeyword == null) {
			return articles;
		}
		
		List<Article> filterdList = new ArrayList<>();
		
		for (Article article : articles) {
			if (article.getTitle().contains(searchKeyword)) {
				filterdList.add(article);
			}
		}
		
		return filterdList;
	}

	// 게시물 추가하기
	public ResultData add(String title, String body) {
		int id = ++articlesLastId;
		
		// 현재 날짜 받기
		String regDate = Util.getNowDateStr();
		String updateDate = regDate;
		
		articles.add(new Article(id, regDate, updateDate, title, body));
		
		return new ResultData("S-1","해당 게시물이 등록되었습니다.","id",id);
	}

	// 게시물 삭제하기
	public ResultData deleteArticle(int id) {
		for (Article article : articles) {
			if (article.getId() == id) {
				articles.remove(article);
				return new ResultData("S-1","해당 게시물이 삭제되었습니다.");
			}
		}
		return new ResultData("F-1","해당 게시물은 존재하지않습니다.");

	}

	public ResultData modify(int id, String title, String body) {
		Article article = getArticle(id);

		article.setTitle(title);
		article.setBody(body);
		article.setUpdateDate(Util.getNowDateStr());
		
		return new ResultData("S-1","해당 게시물이 수정되었습니다.","id",id);
	}
}
