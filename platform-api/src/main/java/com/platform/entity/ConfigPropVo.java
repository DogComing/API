package com.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: platform
 * @description: 道具配置表实体类
 * @author: Yuan
 * @create: 2020-09-09 10:20
 **/
@Data
@ApiModel("道具配置表实体类")
public class ConfigPropVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("道具ID")
    private Integer id;

    @ApiModelProperty("道具等级")
    private Integer grade;

    @ApiModelProperty("道具名称")
    private String propName;

    @ApiModelProperty("道具描述")
    private String propDesc;

    @ApiModelProperty("道具图片")
    private String imgName;

    @ApiModelProperty("道具类型【1:普通道具 2:珍宝道具】")
    private Integer propType;

    @ApiModelProperty("使用场景类型（1:战斗场景 2:其他场景）")
    private Integer useType;

    @ApiModelProperty("道具属性类型【1:强制增加宠物2点成长  2:参赛时免扣5点体力  3:参赛时免交报名费  4:参赛时临时增加战斗力  5:参赛结束后减少宠物冷却时间  6:随机开出1～5级捕捉装备  7:随机开出1～5级品质的狗狗】")
    private Integer attributeType;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;
}
