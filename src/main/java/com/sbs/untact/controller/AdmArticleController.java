package com.sbs.untact.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.sbs.untact.dto.Article;
import com.sbs.untact.dto.Board;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.service.ArticleService;
import com.sbs.untact.service.GenFileService;
import com.sbs.untact.util.Util;

@Controller
public class AdmArticleController extends BaseController{

	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private GenFileService genFileService;

	@RequestMapping("/adm/article/detail")
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

	@RequestMapping("/adm/article/list")
	public String showList(HttpServletRequest req,@RequestParam(defaultValue = "1") int boardId, String searchKeywordType,
			String searchKeyword,@RequestParam(defaultValue = "1") int page) {
		
		Board board = articleService.getBoard(boardId);
		
		if (board == null) {
			return msgAndBack(req, "존재하지 않는 게시판입니다.");
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
		
		req.setAttribute("articles", articles);
		
		return "adm/article/list";
	}
	
	@RequestMapping("/adm/article/add")
	public String showAdd(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		return "adm/article/add";
	}

	@RequestMapping("/adm/article/doAdd")
	@ResponseBody
	public ResultData doAdd(@RequestParam Map<String, Object> param, HttpServletRequest req, MultipartRequest multipartRequest) {
		int loginedMemberId = (int) req.getAttribute("loginedMemberId");

		if (param.get("title") == null) {
			return new ResultData("F-1", "제목을 입력하세요");
		}

		if (param.get("body") == null) {
			return new ResultData("F-1", "내용을 입력하세요");
		}

		param.put("memberId", loginedMemberId);

		ResultData addArticleRd = articleService.addArticle(param);
		
		int newArticleId = (int) addArticleRd.getBody().get("id");
		
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		
		for (String fileInputName : fileMap.keySet()) {
			MultipartFile multipartFile = fileMap.get(fileInputName);
			String[] fileInputNameBits = fileInputName.split("__");
			
			if (fileInputNameBits[0].equals("file")==false) {
				continue;
			}
			int fileSize = (int) multipartFile.getSize();

			if (fileSize <= 0) {
				continue;
			}

			String relTypeCode = fileInputNameBits[1];
			int relId = newArticleId;
			String typeCode = fileInputNameBits[3];
			String type2Code = fileInputNameBits[4];
			int fileNo = Integer.parseInt(fileInputNameBits[5]);
			String originFileName = multipartFile.getOriginalFilename();
			String fileExtTypeCode = Util.getFileExtTypeCodeFromFileName(multipartFile.getOriginalFilename());
			String fileExtType2Code = Util.getFileExtType2CodeFromFileName(multipartFile.getOriginalFilename());
			String fileExt = Util.getFileExtFromFileName(multipartFile.getOriginalFilename()).toLowerCase();
			String fileDir = Util.getNowYearMonthDateStr();

			genFileService.saveMeta(relTypeCode, relId, typeCode, type2Code, fileNo, originFileName, fileExtTypeCode,
					fileExtType2Code, fileExt, fileSize, fileDir);
		}

		return addArticleRd;
	}

	@RequestMapping("/adm/article/doDelete")
	@ResponseBody
	public ResultData doDelete(Integer id,HttpServletRequest req) {
		int loginedMemberId = (int) req.getAttribute("loginedMemberId");

		if (id == null) {
			return new ResultData("F-1", "Id를 입력해주세요.");
		}
				
		Article article = articleService.getArticle(id);
		
		if (article == null) {
			return new ResultData("F-2", "해당 게시물이 존재하지 않습니다.");
		}
		
		ResultData actorCanModifyRd = articleService.getActorCanDeleteRd(article, loginedMemberId);

		if (actorCanModifyRd.isFail()) {
			return actorCanModifyRd;
		}

		return articleService.deleteArticle(id);
	}

	@RequestMapping("/adm/article/doModify")
	@ResponseBody
	public ResultData doModify(Integer id, String title, String body, HttpServletRequest req) {
		int loginedMemberId = (int) req.getAttribute("loginedMemberId");

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
			return new ResultData("F-2", "해당 게시물이 존재하지 않습니다.");
		}

		ResultData actorCanModifyRd = articleService.getActorCanModifyRd(article, loginedMemberId);

		if (actorCanModifyRd.isFail()) {
			return actorCanModifyRd;
		}

		return articleService.modifyArticle(id, title, body);
	}
}
