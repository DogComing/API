package com.platform.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StakeMoreVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 赞助类型 1：单压 2：名次 3：包围
     */
    private Integer type;

    private Map oneMap;

    private Map twoMap;

    private Map threeMap;
}
