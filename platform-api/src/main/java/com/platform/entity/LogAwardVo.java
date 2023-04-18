package com.platform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 押注中奖实体类
 */
@Data
@ApiModel("押注中奖实体类")
public class LogAwardVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Integer id;
    /**
     * 中奖人userId
     */
    @ApiModelProperty("中奖人userId")
    private Long userId;

    /**
     * 押注类型 1:单压 2:名次 3:包围
     */
    @ApiModelProperty("押注类型 1:单压 2:名次 3:包围")
    private Integer stakeType;

    /**
     * 选择类型 独赢：0 前二：2 前三：3 前四：4
     */
    @ApiModelProperty("选择类型 独赢：0 前二：2 前三：3 前四：4")
    private Integer selectType;

    /**
     * 中奖局号
     */
    @ApiModelProperty("中奖局号")
    private String gameNum;

    /**
     * 中奖描述
     */
    @ApiModelProperty("中奖描述")
    private String awardDesc;

    /**
     * 压的几号狗狗
     */
    @ApiModelProperty("压的几号狗狗")
    private String dogNum;

    /**
     * 压的几号赛道（和 压的几号狗狗 同理 只不过服务端计算需要使用）
     */
    @JsonIgnore
    @ApiModelProperty("压的几号赛道（和 压的几号狗狗 同理 只不过服务端计算需要使用）")
    private String trackNum;

    /**
     * 下注数量
     */
    @ApiModelProperty("下注数量")
    private Integer pourNum;

    /**
     * 赔率
     */
    @ApiModelProperty("赔率")
    private BigDecimal odds;

    /**
     * 中奖金额
     */
    @ApiModelProperty("中奖金额")
    private BigDecimal money;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;
}
