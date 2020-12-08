package com.yang.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectTypeVO implements Serializable {
    private Integer id;
    private String name;
    private String remark;

    private List<PortalProjectVO> portalProjectVOList;
}
