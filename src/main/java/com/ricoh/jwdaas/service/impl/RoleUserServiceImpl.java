package com.ricoh.jwdaas.service.impl;

import com.ricoh.jwdaas.dto.MyRoleUser;
import com.ricoh.jwdaas.service.RoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * @author codermy
 * @createTime 2020/7/10
 */
@Service
public class RoleUserServiceImpl implements RoleUserService {

	@Override
	public List<MyRoleUser> getMyRoleUserByUserId(Integer userId) {
		// TODO Auto-generated method stub
		return null;
	}
}
