package com.yupi.yupao.mapper;

import com.yupi.yupao.model.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 73782
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2024-12-05 20:19:14
* @Entity com.yupi.yupao.model.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

    Long selectByOpenId(String openId);
}




