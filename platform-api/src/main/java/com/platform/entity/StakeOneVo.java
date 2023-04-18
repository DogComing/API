package com.platform.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StakeOneVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer dogId;

    private Integer dogNumber;

    private Integer stakeNumber;
}
