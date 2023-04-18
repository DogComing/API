package com.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 比赛结果实体类
 */
@Data
@ApiModel("比赛结果实体类")
public class LogGameResultVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Integer id;

    /**
     * 参赛用户userId
     */
    @ApiModelProperty("参赛用户userId")
    private Long userId;

    /**
     * gameNum
     */
    @ApiModelProperty("每局局号")
    private String gameNum;

    /**
     * 狗狗本场比赛所在赛道 即赞助号
     */
    @ApiModelProperty("狗狗本场比赛所在赛道 即赞助号")
    private Integer dogNumber;

    /**
     * 比赛名次
     */
    @ApiModelProperty("比赛名次")
    private Integer ranking;

    /**
     * 是否真实用户（0:人机  1:真人）
     */
    @ApiModelProperty("是否真实用户（0:人机  1:真人）")
    private Integer isReal;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;
}
