package com.sbs.untact.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberDao {

	public void addMember(Map<String, Object> param);

}
