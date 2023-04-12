package com.ricoh.jwdaas.service;


import com.ricoh.jwdaas.dto.MyRole;
import com.ricoh.jwdaas.dto.RoleDto;
import com.ricoh.jwdaas.utils.Result;

/**
 * @author codermy
 * @createTime 2020/7/10
 */
public interface RoleService {
    /**
     * 返回角色
     * @param startPosition
     * @param limit
     * @param myRole
     * @return
     */
    Result<MyRole> getFuzzyRolesByPage(Integer startPosition, Integer limit, MyRole myRole);

    /**
     * 通过id获得角色信息
     * @param roleId
     * @return
     */
    MyRole getRoleById(Integer roleId);

    /**
     * 更新角色
     * @param roleDto
     * @return
     */
    Result update(RoleDto roleDto);

    /**
     * 数据权限
     * @param roleDto
     * @return
     */
    Result authDataScope(RoleDto roleDto);
    /**
     * 新建角色
     * @param roleDto
     * @return
     */
    Result save(RoleDto roleDto);

    /**
     * 删除角色
     * @param roleId
     * @return
     */
    Result<MyRole> delete(Integer roleId);

    /**
     * 获取全部角色
     * @return
     */
    Result<MyRole> getAllRoles();
}
