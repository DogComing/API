package com.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: platform
 * @description: 捕捉道具配置表实体类
 * @author: Yuan
 * @create: 2020-08-22 16:42
 **/
@Data
@ApiModel("捕捉道具配置表实体类")
public class ConfigCatchEquipVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("捕捉道具ID")
    private Integer id;

    @ApiModelProperty("捕捉装备名称")
    private String equipName;

    @ApiModelProperty("捕捉装备等级")
    private Integer grade;

    @ApiModelProperty("捕捉装备描述")
    private String equipDesc;

    @ApiModelProperty("捕捉装备图片名称")
    private String imgName;

    /**
     * 购买所需的U数量
     */
    @ApiModelProperty("购买所需的U数量")
    private Integer Unum;

    @ApiModelProperty("装备耐久度")
    private Integer durability;

    @ApiModelProperty("捕捉类别【1：抛接  2：藏食  3：啃咬  4：发声】")
    private Integer catchType;

    @ApiModelProperty("作用类别")
    private Integer deedType;

    /**
     * 是否有额外效果【0:否 1:是】
     */
    @ApiModelProperty("是否有额外效果【0:否 1:是】")
    private Integer extraOne;

    /**
     * 是否有额外效果【0:否 1:是】
     */
    @ApiModelProperty("是否有额外效果【0:否 1:是】")
    private Integer extraTwo;

    @ApiModelProperty("是否珍宝【0:否 1:是】")
    private Integer isGem;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;
}
