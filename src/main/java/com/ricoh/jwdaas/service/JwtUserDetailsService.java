/**  

* <p>Title: JwtUserDetailsService.java</p>  

* <p>Description: </p>  

* <p>Copyright: Copyright (c) 2023</p>  

* <p>Company: RICOH</p>  

* @author jwChen  

* @date 2023年3月28日  

* @version 1.0  

*/  
package com.ricoh.jwdaas.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ricoh.jwdaas.dto.JwtUserDto;
import com.ricoh.jwdaas.dto.MyRole;
import com.ricoh.jwdaas.dto.MyRoleUser;
import com.ricoh.jwdaas.dto.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class JwtUserDetailsService implements UserDetailsService {
 
	@Autowired
    private UserService userService;

    @Autowired
    private RoleUserService roleUserService;

    @Autowired
    private RoleService roleService;

//    @Autowired
//    private MenuDao menuDao;

    @Override
    public JwtUserDto loadUserByUsername(String userName){
        // 根据用户名获取用户
        MyUser user = userService.getUserByName(userName);
        if (user == null ){
            throw new BadCredentialsException("用户名或密码错误");
        }else if (user.getStatus().equals(MyUser.Status.LOCKED)) {
            throw new LockedException("用户被锁定,请联系管理员解锁");
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for(int i=0;i<10;i++) {
        	GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(String.valueOf(i));
        	grantedAuthorities.add(grantedAuthority);
        }
        System.out.println(grantedAuthorities.toString());
//        List<MenuIndexDto> list = menuDao.listByUserId(user.getUserId());
//        List<String> collect = list.stream().map(MenuIndexDto::getPermission).collect(Collectors.toList());
//        for (String authority : collect){
//            if (!("").equals(authority) & authority !=null){
//                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority);
//                grantedAuthorities.add(grantedAuthority);
//            }
//        }
        //将用户所拥有的权限加入GrantedAuthority集合中
        JwtUserDto loginUser =new JwtUserDto(user,grantedAuthorities);
//        loginUser.setRoleInfo(getRoleInfo(user));
        return loginUser;
    }


    public List<MyRole> getRoleInfo(MyUser myUser) {
        MyUser userByName = userService.getUserByName(myUser.getUserName());
        List<MyRoleUser> roleUserByUserId = roleUserService.getMyRoleUserByUserId(userByName.getUserId());
        List <MyRole> roleList = new ArrayList<>();
        for (MyRoleUser roleUser:roleUserByUserId){
            Integer roleId = roleUser.getRoleId();
            MyRole roleById = roleService.getRoleById(roleId);
            roleList.add(roleById);
        }
        return roleList;
    }
}

