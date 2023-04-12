package com.ricoh.jwdaas.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ricoh.jwdaas.dto.JwtUserDto;
import com.ricoh.jwdaas.dto.LoginForm;
import com.ricoh.jwdaas.service.JwtUserDetailsService;
import com.ricoh.jwdaas.utils.JwtUtil;
import com.ricoh.jwdaas.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

	@Autowired
    JwtUserDetailsService jwtUserDetailsService;
	
	@Autowired
    private JwtUtil jwtUtil;
    @Value("${trustdomain}")
    private String trustdomain;
	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
    @GetMapping("/user")
    public String user(){
        return "me";
    }
    
    @PostMapping("/user1")
    public Result user1(HttpServletRequest req, HttpServletResponse res){
//    	System.out.println("resss:"+res.getHeader("Authorization"));
    	List<String> list = new ArrayList<String>();
    	for(int i = 0;i<10;i++) {
    		list.add(UUID.randomUUID().toString());
    	}
    	Result result = Result.ok().message("登录成功").data(list).jwt(res.getHeader("Authorization"));
        return result;
    }
    
    @PostMapping("/addUser")
    public Result addUser(@RequestBody LoginForm loginForm){
    	String encode = passwordEncoder.encode("123456");//密码加密操作-将加密之后的结果存储到数据库中
        String encode1 = passwordEncoder.encode("123456");
        System.out.println(encode); // $2a$10$J0HqjTj2g98KceRapnRqW.Y4uQkPzGASFstgx1yba2JQG1muHj7L2
        System.out.println(encode1); //$2a$10$foxH4yrARWiaWwxyTXjFMOkSqkiOXsMaiQ6/oWbxxond5/BZqi1ke
//    	Result result = Result.ok().message("登录成功").data(list).jwt(res.getHeader("Authorization"));
//        return result;
        return null;
    }
    
    @PostMapping("/logout")
    @ResponseBody
    public String user1(){
    	System.out.println("退出登录");
    	return "退出登录";
    }
    @PostMapping("/login")
    public Result authenticateUser(@RequestBody LoginForm loginForm) {
    	UserDetails a = jwtUserDetailsService.loadUserByUsername(loginForm.getUsername());
    	try {
    		Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginForm.getUsername(),
                            loginForm.getPassword()
                    )
            );
    		JwtUserDto userDetails = (JwtUserDto)authentication.getPrincipal();//拿到登录用户信息
    		String jwtToken = jwtUtil.generateToken(userDetails,trustdomain);//生成token
            Result result = Result.ok().message("登录成功").jwt(jwtToken);
//            return ResponseEntity.ok(result);
    		return result;
		} catch (BadCredentialsException e) {
			// TODO: handle exception
			Result result = Result.error().code(20001).message("用户名或密码错误");
			return result;
		}
    	
        
    }
    	
        // Authenticate user
        
}
