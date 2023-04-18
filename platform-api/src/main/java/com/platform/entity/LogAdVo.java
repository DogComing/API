package com.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户观看广告记录实体类
 */
@Data
@ApiModel("用户观看广告记录实体类")
public class LogAdVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("广告ID")
    private Integer adId;

    @ApiModelProperty("观看用户ID")
    private Long userId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;
}
