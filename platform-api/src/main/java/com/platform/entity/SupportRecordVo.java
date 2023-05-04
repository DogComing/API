package com.platform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupportRecordVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("自增主键")
    private Long id;

    @ApiModelProperty("局号")
    private String gameNum;

    @ApiModelProperty("用户昵称")
    private String userName;

    @ApiModelProperty("用户ID")
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

    @ApiModelProperty("压的的狗狗ID")
    private String dogId;

    @ApiModelProperty("压的几号狗狗")
    private String dogNum;

    /**
     * 压的几号狗狗
     */
    @JsonIgnore
    @ApiModelProperty("压的几号狗狗")
    private String trackNum;

    @JsonIgnore
    @ApiModelProperty("本注赔率")
    private BigDecimal odds;

    @ApiModelProperty("下注数量")
    private Integer pourNum;

    @JsonIgnore
    @ApiModelProperty("钱包地址")
    private String address;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;
}
