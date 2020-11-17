package com.yang.crowd.mapper;

import com.yang.crowd.entity.Auth;
import com.yang.crowd.entity.AuthExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface AuthMapper {
    int countByExample(AuthExample example);

    int deleteByExample(AuthExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Auth record);

    int insertSelective(Auth record);

    List<Auth> selectByExample(AuthExample example);

    Auth selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Auth record, @Param("example") AuthExample example);

    int updateByExample(@Param("record") Auth record, @Param("example") AuthExample example);

    int updateByPrimaryKeySelective(Auth record);

    int updateByPrimaryKey(Auth record);

    List<Integer> getAssignedAuthByRoleId(@Param("roleId")Integer roleId);

    void deleteOldRelationship(@Param("roleId")Integer roleId);

    void insertNewRelationship(@Param("roleId")Integer roleId,@Param("authIdList")List<Integer> authIdList);
}