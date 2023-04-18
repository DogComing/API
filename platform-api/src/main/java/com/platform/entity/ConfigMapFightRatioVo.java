package com.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: platform
 * @description: 地图产出各等级对战道具概率实体类
 * @author: Yuan
 * @create: 2020-08-22 16:42
 **/
@Data
@ApiModel("地图产出各等级对战道具概率实体类")
public class ConfigMapFightRatioVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Integer id;

    @ApiModelProperty("地图ID")
    private Integer mapId;

    @ApiModelProperty("一级对战道具产出概率")
    private String equipGrade01;
    @ApiModelProperty("二级对战道具产出概率")
    private String equipGrade02;
    @ApiModelProperty("三级对战道具产出概率")
    private String equipGrade03;
    @ApiModelProperty("四级对战道具产出概率")
    private String equipGrade04;
    @ApiModelProperty("五级对战道具产出概率")
    private String equipGrade05;
    @ApiModelProperty("六级对战道具产出概率")
    private String equipGrade06;
    @ApiModelProperty("七级对战道具产出概率")
    private String equipGrade07;
    @ApiModelProperty("八级对战道具产出概率")
    private String equipGrade08;
    @ApiModelProperty("九级对战道具产出概率")
    private String equipGrade09;
    @ApiModelProperty("十级对战道具产出概率")
    private String equipGrade10;
}
