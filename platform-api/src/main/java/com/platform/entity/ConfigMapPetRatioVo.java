package com.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: platform
 * @description: 地图产出各品质狗狗概率实体类
 * @author: Yuan
 * @create: 2020-08-22 16:42
 **/
@Data
@ApiModel("地图产出各品质狗狗概率实体类")
public class ConfigMapPetRatioVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Integer id;

    @ApiModelProperty("地图ID")
    private Integer mapId;

    @ApiModelProperty("一级宠物产出概率")
    private Integer petGrade01;
    @ApiModelProperty("二级宠物产出概率")
    private Integer petGrade02;
    @ApiModelProperty("三级宠物产出概率")
    private Integer petGrade03;
    @ApiModelProperty("四级宠物产出概率")
    private Integer petGrade04;
    @ApiModelProperty("五级宠物产出概率")
    private Integer petGrade05;
    @ApiModelProperty("六级宠物产出概率")
    private Integer petGrade06;
    @ApiModelProperty("七级宠物产出概率")
    private Integer petGrade07;
    @ApiModelProperty("八级宠物产出概率")
    private Integer petGrade08;
    @ApiModelProperty("九级宠物产出概率")
    private Integer petGrade09;
    @ApiModelProperty("十级宠物产出概率")
    private Integer petGrade10;
}
