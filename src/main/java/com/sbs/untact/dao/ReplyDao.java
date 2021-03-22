package com.sbs.untact.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sbs.untact.dto.Reply;

@Mapper
public interface ReplyDao {

	void addReply(Map<String, Object> param);

	List<Reply> getForPrintReplies(@Param(value="relTypeCode") String relTypeCode,
			@Param(value="relId") Integer relId);

	Reply getReply(@Param(value="id") Integer id);

	void deleteReply(@Param(value="id") Integer id);

	void modifyReply(@Param(value="id") int id,@Param(value="body") String body);
}
