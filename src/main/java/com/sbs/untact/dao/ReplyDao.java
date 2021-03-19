package com.sbs.untact.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sbs.untact.dto.Reply;

@Mapper
public interface ReplyDao {

	public void addReply(Map<String, Object> param);

	public List<Reply> getForPrintReplies(@Param(value="relTypeCode") String relTypeCode,
			@Param(value="relId") Integer relId);
}
