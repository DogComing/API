package com.platform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("邮件实体类")
public class MailVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("邮件ID,自增主键")
    private Integer id;

    /**
     * 邮件标题
     */
    @ApiModelProperty("邮件标题")
    private String mailTitle;

    /**
     * 邮件内容
     */
    @ApiModelProperty("邮件内容")
    private String mailContent;

    /**
     * 签到奖励数量
     */
    @ApiModelProperty("签到奖励数量")
    private Integer awardNum;

    /**
     * 精力数量（参赛奖励用的）
     */
    @JsonIgnore
    @ApiModelProperty("精力数量（参赛奖励用的）")
    private Integer brawnNum;

    /**
     * 比赛奖励（参赛奖励用的）
     */
    @ApiModelProperty("比赛奖励（参赛奖励用的）")
    private String gameAward;

    /**
     * 邮件类型 1：普通邮件 2：结算邮件
     */
    @ApiModelProperty("邮件类型 1：普通邮件 2：结算邮件")
    private Integer type;

    /**
     * 奖励图片名称
     */
    @ApiModelProperty("奖励图片名称")
    private String imgName;

    /**
     * 是否道具/饲料/装备【0:否  1:是】
     */
    @ApiModelProperty("是否道具/饲料/装备【0:否  1:是】")
    private Integer isAttribute;

    /**
     * 签到奖励类型【1:代币  2:精力  3:饲料  4:道具  5:捕捉装备 6:战斗装备】
     */
    @ApiModelProperty("签到奖励类型【1:代币  2:精力  3:饲料  4:道具  5:捕捉装备 6:战斗装备】")
    private Integer awardType;

    /**
     * 种类ID【例如：饲料表 一级饲料对应的ID；道具表 某个道具对应的ID】
     */
    @ApiModelProperty("种类ID【例如：饲料表 一级饲料对应的ID；道具表 某个道具对应的ID】")
    private Integer kindId;

    /**
     * 是否已领取【0:否 1:是】
     */
    @ApiModelProperty("是否已领取【0:否 1:是】")
    private Integer isReceive;

    /**
     * 是否已读【0:否 1:是】
     */
    @ApiModelProperty("是否已读【0:否 1:是】")
    private Integer isRead;

    /**
     * 用户UserId
     */
    @ApiModelProperty("用户UserId")
    private Long userId;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;


}
