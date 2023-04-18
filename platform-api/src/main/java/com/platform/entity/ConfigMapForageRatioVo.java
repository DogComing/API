package com.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: platform
 * @description: 地图产出各等级饲料概率实体类
 * @author: Yuan
 * @create: 2020-08-22 16:42
 **/
@Data
@ApiModel("地图产出各等级饲料概率实体类")
public class ConfigMapForageRatioVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Integer id;

    @ApiModelProperty("地图ID")
    private Integer mapId;

    @ApiModelProperty("一级捕饲料产出概率")
    private Integer forageGrade01;
    @ApiModelProperty("二级捕饲料产出概率")
    private Integer forageGrade02;
    @ApiModelProperty("三级捕饲料产出概率")
    private Integer forageGrade03;
    @ApiModelProperty("四级捕饲料产出概率")
    private Integer forageGrade04;
    @ApiModelProperty("五级捕饲料产出概率")
    private Integer forageGrade05;
    @ApiModelProperty("六级捕饲料产出概率")
    private Integer forageGrade06;
    @ApiModelProperty("七级捕饲料产出概率")
    private Integer forageGrade07;
    @ApiModelProperty("八级捕饲料产出概率")
    private Integer forageGrade08;
    @ApiModelProperty("九级捕饲料产出概率")
    private Integer forageGrade09;
    @ApiModelProperty("十级捕饲料产出概率")
    private Integer forageGrade10;
}
