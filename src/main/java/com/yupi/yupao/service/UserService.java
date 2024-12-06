package com.yupi.yupao.service;

import com.yupi.yupao.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yupao.model.dto.WeChatCodeDTO;
import com.yupi.yupao.model.vo.UsersLoginVO;

/**
* @author 73782
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-12-05 20:19:14
*/
public interface UserService extends IService<User> {

    UsersLoginVO authWechat(WeChatCodeDTO code);
}
