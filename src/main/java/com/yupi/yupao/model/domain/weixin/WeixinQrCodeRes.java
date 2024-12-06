package com.yupi.yupao.model.domain.weixin;

import lombok.Data;

/**
 * @ClassName WeixinQrCodeRes
 * @Description:
 * @Author: 橘子皮
 * @CreateDate: 2024/11/20 14:00
 */
@Data
public class WeixinQrCodeRes {
    private String ticket;
    private Long expire_seconds;
    private String url;
}
