package com.platform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户饲料实体类
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("用户饲料实体类")
public class UserForageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("自增主键Id")
    private Integer id;

    /**
     * 饲料等级
     */
    @ApiModelProperty("饲料等级")
    private Integer grade;

    /**
     * 用户userId
     */
    @ApiModelProperty("用户userId")
    private Long userId;

    /**
     * 饲料ID
     */
    @ApiModelProperty("饲料ID")
    private Integer forageId;

    /**
     * 饲料名称
     */
    @ApiModelProperty("饲料名称")
    private String forageName;

    /**
     * 饲料描述
     */
    @ApiModelProperty("饲料描述")
    private String forageDesc;

    /**
     * 饲料图片名称
     */
    @ApiModelProperty("饲料图片名称")
    private String imgName;

    /**
     * 增加战力点数
     */
    @ApiModelProperty("增加战力点数")
    private Integer fightingNum;

    /**
     * 饲料类型【1:普通饲料 2:珍宝饲料】
     */
    @ApiModelProperty("饲料类型【1:普通饲料 2:珍宝饲料】")
    private Integer forageType;

    /**
     * 是否忽略天赋【0:否 1:是】
     */
    @ApiModelProperty("是否忽略天赋【0:否 1:是】")
    private Integer isIgnoreTalent;

    /**
     * 饲料数量
     */
    @ApiModelProperty("饲料数量")
    private Integer forageNum;

    /**
     * 是否NFT（0:否 1:是）
     */
    @ApiModelProperty("是否NFT（0:否 1:是）")
    private Integer isNft;

    /**
     * 是否已提取到基地【0:否 1:是】
     */
    @ApiModelProperty("是否已提取到基地【0:否 1:是】")
    private Integer isDraw;

    /**
     * 是否冻结【0否 1:是】
     */
    @JsonIgnore
    @ApiModelProperty("是否冻结【0否 1:是】")
    private Integer isFreeze;

    /**
     * NFT的ID，唯一序号
     */
    @JsonIgnore
    @ApiModelProperty("NFT的ID，唯一序号")
    private Long nftId;

    @JsonIgnore
    @ApiModelProperty("创建时间")
    private Date createTime;

    @JsonIgnore
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 1: 宠物 2；珍宝 3：饲料 4：野生 5：捕捉装备 6：对战装备 7：失败
     */
    private Integer type;

    /**
     * NFT类型：1：宠物、2：道具、3：饲料、4：野生、5：捕捉装备 6：对战装备
     */
    private Integer nftType;

    /**
     * 丢弃类型 1：宠物、2：道具、3：饲料、4：野生、5：捕捉装备 6：对战装备
     */
    private Integer discardType;

    /**
     * 饲料类型【string类型 文字展示】
     * 1:普通饲料 2:珍宝饲料
     */
    private String forageTypeTxt;

    public String getForageTypeTxt() {

        if (null != forageType && forageType != 0 && StringUtils.isEmpty(forageTypeTxt)) {

            forageTypeTxt = "";
            switch (forageType) {
                case 1:
                    forageTypeTxt = "普通饲料";
                    break;
                case 2:
                    forageTypeTxt = "珍宝饲料";
                    break;
            }
        }
        return forageTypeTxt;
    }
}
