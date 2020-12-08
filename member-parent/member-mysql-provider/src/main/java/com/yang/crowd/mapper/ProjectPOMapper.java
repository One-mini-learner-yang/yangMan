package com.yang.crowd.mapper;

import java.util.List;

import com.yang.crowd.entity.po.ProjectPO;
import com.yang.crowd.entity.po.ProjectPOExample;
import com.yang.crowd.entity.vo.DetailProjectVO;
import com.yang.crowd.entity.vo.ProjectTypeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
public interface ProjectPOMapper {
    int countByExample(ProjectPOExample example);

    int deleteByExample(ProjectPOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ProjectPO record);

    int insertSelective(ProjectPO record);

    List<ProjectPO> selectByExample(ProjectPOExample example);

    ProjectPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ProjectPO record, @Param("example") ProjectPOExample example);

    int updateByExample(@Param("record") ProjectPO record, @Param("example") ProjectPOExample example);

    int updateByPrimaryKeySelective(ProjectPO record);

    int updateByPrimaryKey(ProjectPO record);

    void insertTypeRelationShip(@Param("typeIdList") List<Integer> typeIdList,@Param("projectId") Integer projectId);

    void insertTagRelationship(@Param("tagIdList") List<Integer> tagIdList,@Param("projectId") Integer projectId);

    List<ProjectTypeVO> selectPortalTypeVOList();

    DetailProjectVO selectDetailProjectVO(@Param("projectid")Integer projectId);
}