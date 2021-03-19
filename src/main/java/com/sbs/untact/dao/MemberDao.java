package com.sbs.untact.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sbs.untact.dto.Member;

@Mapper
public interface MemberDao {

	public void addMember(Map<String, Object> param);
	
	public Member getMemberById(@Param(value="id") int id);	
	
	public Member getMemberByLoginId(@Param(value="loginId") String loginId);

}
