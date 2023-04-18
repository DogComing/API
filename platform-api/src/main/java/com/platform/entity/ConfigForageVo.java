package com.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: platform
 * @description: 饲料配置表实体类
 * @author: Yuan
 * @create: 2020-09-09 10:20
 **/
@Data
@ApiModel("饲料配置表实体类")
public class ConfigForageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("饲料ID")
    private Integer id;

    @ApiModelProperty("饲料等级")
    private Integer grade;

    @ApiModelProperty("饲料名称")
    private String forageName;

    @ApiModelProperty("饲料描述")
    private String forageDesc;

    @ApiModelProperty("饲料图片名称")
    private String imgName;

    @ApiModelProperty("增加战力点数")
    private Integer fightingNum;

    @ApiModelProperty("饲料类型【1:普通饲料 2:珍宝饲料】")
    private Integer forageType;

    @ApiModelProperty("是否忽略天赋【0:否 1:是】")
    private Integer isIgnoreTalent;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;
}
