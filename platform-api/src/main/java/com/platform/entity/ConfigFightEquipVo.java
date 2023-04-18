package com.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: platform
 * @description: 对战装备配置表实体类
 * @author: Yuan
 * @create: 2020-08-22 16:42
 **/
@Data
@ApiModel("对战装备配置表实体类")
public class ConfigFightEquipVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("对战装备ID")
    private Integer id;

    @ApiModelProperty("对战装备等级")
    private Integer grade;

    @ApiModelProperty("对战装备名称")
    private String fightName;

    @ApiModelProperty("对战装备描述")
    private String fightDesc;

    @ApiModelProperty("对战装备图片名称")
    private String imgName;

    @ApiModelProperty("战力加成百分比")
    private BigDecimal fightingAddition;

    @ApiModelProperty("是否珍宝【0:否 1:是】")
    private Integer isGem;

    @ApiModelProperty("对战装备类型（1:狗绳 2:狗铃 3:狗披风）")
    private Integer fightType;

    @ApiModelProperty("装备耐久度")
    private Integer durability;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;
}
