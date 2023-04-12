package com.ricoh.jwdaas.service.impl;

import com.ricoh.jwdaas.dto.MyRole;
import com.ricoh.jwdaas.dto.RoleDto;
import com.ricoh.jwdaas.service.RoleService;
import com.ricoh.jwdaas.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.util.List;

/**
 * @author codermy
 * @createTime 2020/7/10
 */
@Service
public class RoleServiceImpl implements RoleService {

	@Override
	public Result<MyRole> getFuzzyRolesByPage(Integer startPosition, Integer limit, MyRole myRole) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MyRole getRoleById(Integer roleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result update(RoleDto roleDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result authDataScope(RoleDto roleDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result save(RoleDto roleDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result<MyRole> delete(Integer roleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result<MyRole> getAllRoles() {
		// TODO Auto-generated method stub
		return null;
	}
    
}
