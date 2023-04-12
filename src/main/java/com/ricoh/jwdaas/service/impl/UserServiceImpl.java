package com.ricoh.jwdaas.service.impl;

import com.ricoh.jwdaas.dto.MyUser;
import com.ricoh.jwdaas.mapper.UserMapper;
import com.ricoh.jwdaas.service.UserService;
import com.ricoh.jwdaas.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.ArrayList;
import java.util.List;

/**
 * @author codermy
 * @createTime 2020/7/10
 */
@Service
public class UserServiceImpl implements UserService {
	@Autowired
    private UserMapper userMapper;
	@Override
	public Result<MyUser> getAllUsersByPage(Integer offectPosition, Integer limit, MyUser myUser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MyUser getUserById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void checkUserAllowed(MyUser user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String checkPhoneUnique(MyUser user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String checkUserNameUnique(MyUser user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result<MyUser> updateUser(MyUser myUser, Integer roleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int changeStatus(MyUser user) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Result<MyUser> save(MyUser myUser, Integer roleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteUser(Integer userId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MyUser getUserByName(String userName) {
		// TODO Auto-generated method stub
		return userMapper.getUser(userName);
	}
    
}
