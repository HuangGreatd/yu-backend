package com.yupi.yupao.service;

import com.yupi.yupao.model.domain.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yupao.model.domain.User;
import com.yupi.yupao.model.dto.TeamQuery;
import com.yupi.yupao.model.request.TeamJoinRequest;
import com.yupi.yupao.model.vo.TeamUserVO;

import java.util.List;

/**
* @author 73782
* @description 针对表【team(team)】的数据库操作Service
* @createDate 2024-12-05 20:19:14
*/
public interface TeamService extends IService<Team> {

    long addTeam(Team team, User loginUser);


    /**
     * 搜索队伍
     *
     * @param teamQuery
     * @param isAdmin
     * @return
     */
    List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin);

}
