package com.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: platform
 * @description: 野生配置表实体类
 * @author: Yuan
 * @create: 2020-08-22 16:42
 **/
@Data
@ApiModel("野生配置表实体类")
public class ConfigWildVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("野生ID")
    private Integer id;

    @ApiModelProperty("野生等级")
    private Integer grade;

    @ApiModelProperty("野生名称")
    private String wildName;

    @ApiModelProperty("野生描述")
    private String wildDesc;

    @ApiModelProperty("野生图片名称")
    private String imgName;

    @ApiModelProperty("耐久度")
    private Integer durability;

    @ApiModelProperty("增加战力点数")
    private BigDecimal fightingNum;

    @ApiModelProperty("是否可战斗使用【0:否 1:是】")
    private Integer isFight;

    @ApiModelProperty("野生类型（1:破绳子 2:破鞋子 3:破抹布 4:其他）")
    private Integer wildType;

    @ApiModelProperty("是否忽略天赋（0:否 1:是）")
    private Integer isIgnoreTalent;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;
}
