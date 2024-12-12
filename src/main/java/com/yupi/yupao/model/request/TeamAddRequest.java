package com.yupi.yupao.model.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 创建队伍请求体
 */
@Data
public class TeamAddRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;


    /**
     * 码头
     */
    private String teamWharf;

    /**
     * 描述，例如 ： 一男一女，寻找女士拼一筏
     */
    private String teamDesc;


    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 重量：例如 194kg
     */
    private Double weight;

    /**
     * 手机号
     */
    private String phone;
}
