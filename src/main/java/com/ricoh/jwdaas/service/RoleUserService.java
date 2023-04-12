package com.ricoh.jwdaas.service;


import com.ricoh.jwdaas.dto.MyRoleUser;

import java.util.List;


/**
 * @author codermy
 * @createTime 2020/7/13
 */
public interface RoleUserService {
    /**
     * 返回用户拥有的角色
     * @param userId
     * @return
     */
    List<MyRoleUser> getMyRoleUserByUserId(Integer userId);
}
