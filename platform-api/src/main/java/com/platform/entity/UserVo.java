package com.platform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @author jianxi
 * @email 249893127@qq.com
 * @date 2017-08-15 08:03:41
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("用户数据实体类")
public class UserVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户userId
     */
    @ApiModelProperty("用户userId")
    private Long userId;

    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    private String userName;

    /**
     * 用户头像
     */
    @ApiModelProperty("用户头像")
    private String userAvatar;

    /**
     * 狗币/代币
     */
    @ApiModelProperty("狗币/代币")
    private Integer dogCoin;

    /**
     * 总体力值
     */
    @ApiModelProperty("总体力值")
    private Integer totalMuscleNum;

    /**
     * 剩余体力值
     */
    @ApiModelProperty("剩余体力值")
    private Integer residueMuscleNum;

    /**
     * 当天消耗体力点数
     */
    @JsonIgnore
    @ApiModelProperty("当天消耗体力点数")
    private Integer daysUseBrawn;

    /**
     * 当天剩余购买体力次数
     */
    @JsonIgnore
    @ApiModelProperty("当天剩余购买体力次数")
    private Integer buyBrawnNum;

    /**
     * 是否开启音乐【1:已开启 2:未开启】
     */
    @ApiModelProperty("是否开启音乐【1:已开启 2:未开启】")
    private Integer isMusic;

    /**
     * 是否开启音效【1:已开启 2:未开启】
     */
    @ApiModelProperty("是否开启音效【1:已开启 2:未开启】")
    private Integer isEffect;

    /**
     * 用户状态【1:正常 2:禁用】
     */
    @ApiModelProperty("用户状态【1:正常 2:禁用】")
    private Integer status;

    /**
     * 游戏内语言 【中文：zh 英文：en】
     */
    @ApiModelProperty("游戏内语言 【中文：zh 英文：en】")
    private String language;

    /**
     * 是否免费编辑名字（0:否 1:是）
     */
    @ApiModelProperty("是否免费编辑名字（0:否 1:是）")
    private Integer isFreeNameEdit;

    /**
     * 是否已购买签到（0:否 1:是）
     */
    @ApiModelProperty("是否已购买签到（0:否 1:是）")
    private Integer isSignIn;

    /**
     * 签到天数
     */
    @ApiModelProperty("签到天数")
    private Integer signInDayNum;

    /**
     * 距离购买签到已过天数
     */
    @JsonIgnore
    @ApiModelProperty("距离购买签到已过天数")
    private Integer buyDayNum;

    /**
     * 今天是否签到 【1:是 2:否】
     */
    @ApiModelProperty("今天是否签到 【1:是 2:否】")
    private Integer isTodayCheck;

    /**
     * 是否有未读邮件【1:有 2:没有】
     */
    @ApiModelProperty("是否有未读邮件【1:有 2:没有】")
    private Integer isHaveUnread;

    /**
     * 钱包登录凭证
     */
    @ApiModelProperty("钱包登录凭证")
    private String logonCredentials;

    /**
     * 当前所在地图ID
     */
    @ApiModelProperty("当前所在地图ID")
    private Integer mapId;

    /**
     * 开启地图数量 及 开启最大地图id
     */
    @ApiModelProperty("当前开启地图数量 及 当前开启最大地图id")
    private Integer openMapNum;

    @ApiModelProperty("藏食装备等级")
    private Integer equipHideGrade;

    @ApiModelProperty("啃咬装备等级")
    private Integer equipGnawGrade;

    @ApiModelProperty("发声装备等级")
    private Integer equipSoundGrade;

    @JsonIgnore
    @ApiModelProperty("基地openid")
    private String openId;

    @JsonIgnore
    @ApiModelProperty("基地address")
    private String address;

    @JsonIgnore
    @ApiModelProperty("基地role")
    private Integer role;

    @JsonIgnore
    @ApiModelProperty("基地role_nft")
    private Integer roleNft;

    /**
     * 比赛赢次数
     */
    @ApiModelProperty("比赛赢次数")
    private Integer winNum;

    @JsonIgnore
    @ApiModelProperty("注册Ip")
    private String registerIp;

    @JsonIgnore
    @ApiModelProperty("注册时间")
    private Date registerTime;

    @JsonIgnore
    @ApiModelProperty("最后登录Ip")
    private String lastLoginIp;

    @JsonIgnore
    @ApiModelProperty("最后登录时间")
    private Date lastLoginTime;

    @JsonIgnore
    @ApiModelProperty("下一次购买装备时间")
    private Date nextBuyEquipTime;

    @JsonIgnore
    @ApiModelProperty("上一次恢复精力时间")
    private Date lastResumeTime;

    /**
     * 道具数量
     */
    private Integer dogNum;

    /**
     * 珍宝数量
     */
    private Integer gemNum;

    /**
     * 出战狗狗信息
     */
    private Map dogMap;
}
