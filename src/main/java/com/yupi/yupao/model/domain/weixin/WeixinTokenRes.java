package com.yupi.yupao.model.domain.weixin;

import lombok.Data;

/**
 * @ClassName WeixinTokenRes
 * @Description:
 * @Author: 橘子皮
 * @CreateDate: 2024/11/20 13:55
 */
@Data
public class WeixinTokenRes {
    private String access_token;
    private int expires_in;
    private String errcode;
    private String errmsg;
}
