package com.funeral.vo;

import lombok.Data;

import java.util.List;

@Data
public class ProcessStepDetailVO {
    private Integer id;
    private String title;
    private String content;
    private Integer stepOrder;
    private String imageUrl;
    private Integer parentId;
    private List<ProductVO> productList;
}
