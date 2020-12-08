package com.yang.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortalProjectVO {
    private Integer projectId;
    private String projectName;
    private String headPicturePath;
    private Long money;
    private String deployDate;
    private Integer percentage;
    private Integer supporter;
}
