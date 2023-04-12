/**  

* <p>Title: JwtRequestFilter.java</p>  

* <p>Description: </p>  

* <p>Copyright: Copyright (c) 2023</p>  

* <p>Company: RICOH</p>  

* @author jwChen  

* @date 2023年3月27日  

* @version 1.0  

*/  
package com.ricoh.jwdaas.filter;
import cn.hutool.json.JSONObject;
import com.ricoh.jwdaas.utils.JwtUtil;
import com.ricoh.jwdaas.utils.ResultCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;



import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService jwtUserDetailsService;

//    @Autowired
//    private SecurityConfig securityConfig;

//    @Autowired
//    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");
        System.out.println(requestTokenHeader);
        String username = null;
        String jwtToken = null;
        	    // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        	    if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
        	        jwtToken = requestTokenHeader.substring(7);
//        	        jwtUtil.validateToken(jwtToken);
//        	        System.out.println(jwtToken);
        	        try {
        	            username = jwtUtil.extractUsername(jwtToken);
//        	            jwtUtil.validateToken(jwtToken,jwtToken);
        	        } catch (IllegalArgumentException e) {
        	            logger.error("Unable to get JWT Token");
        	            Map<String, Object> map = new HashMap<String, Object>();
        	        	map.put("code", 20004);
        	        	map.put("msg", "Unable to get JWT Token");
        	        	responseServiceException(response, map, ResultCode.UNAUTHORIZED);
                        return;
        	        } catch (ExpiredJwtException e) {
        	            logger.error("JWT Token has expired");
        	            Map<String, Object> map = new HashMap<String, Object>();
        	        	map.put("code", 20003);
        	        	map.put("msg", "JWT Token has expired");
        	        	responseServiceException(response, map,ResultCode.UNAUTHORIZED);
                        return;
        	        } catch(SignatureException e) {
        	        	Map<String, Object> map = new HashMap<String, Object>();
        	        	map.put("code", 20002);
        	        	map.put("msg", "JWT signature does not match locally computed signature");
        	        	responseServiceException(response, map,ResultCode.UNAUTHORIZED);
                        return;
//        	        	logger.error("JWT Token SignatureException");
        	        }
        	    } else {
        	        logger.warn("JWT Token does not begin with Bearer String");
//        	        Map<String, Object> map = new HashMap<String, Object>();
//    	        	map.put("code", 20005);
//    	        	map.put("msg", "JWT Token does not begin with Bearer String");
//    	        	responseServiceException(response, map,ResultCode.UNAUTHORIZED);
//                    return;
        	    }
        	    

        	    // Once we get the token validate it.
        	    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        	        UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
        	        String origin = request.getHeader("Origin");
        	        System.out.println("输出来源地址："+origin);
        	        // if token is valid configure Spring Security to manually set
        	        // authentication
        	        if (jwtUtil.validateToken(jwtToken, userDetails,origin)) {
        	        	System.out.println("验证JWT");
        	        	if(jwtUtil.isTokenRefreshable(jwtToken)) {
        	        		System.out.println("是否刷新JWT");
            	        	String refreshedToken = jwtUtil.refreshToken(jwtToken);
                            response.setHeader("Authorization", refreshedToken);
            	        }
        	            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
        	                    userDetails, null, userDetails.getAuthorities());
        	            usernamePasswordAuthenticationToken
        	                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        	            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        	        }
        	    }
        	    chain.doFilter(request, response);
        	}

    private void responseServiceException(HttpServletResponse response, Map<String, Object> map,int status) {

//        AuthenticationServiceException serviceError = new AuthenticationServiceException(exceptionMsg);
//        JSONObject responseJSONObject = JSONObject.parseObject(map.toString());
    	JSONObject json = new JSONObject(map);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(status);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(json.toString());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

}