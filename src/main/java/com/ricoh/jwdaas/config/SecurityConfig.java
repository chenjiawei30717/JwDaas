package com.ricoh.jwdaas.config;

import com.ricoh.jwdaas.filter.JwtRequestFilter;
import com.ricoh.jwdaas.handler.MyAuthenticationSuccessHandler;
import com.ricoh.jwdaas.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MyAuthenticationSuccessHandler authenticationSuccessHandler;
//    @Autowired
//    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;
//    @Autowired
//    private CustomCorsFilter customCorsFilter;

//    @Autowired
//    private JwtUtil jwtUtil;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // 配置需要进行身份验证的请求路径
        httpSecurity.csrf().disable()

                .authorizeRequests().antMatchers("/user","/login","/logout").permitAll()
//                .requestMatcher(new TrustedDomainsRequestMatcher())

                .anyRequest().authenticated()

//                .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .formLogin().disable()
                .httpBasic().disable()
                .logout()
                .logoutUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout","POST"))
                .logoutSuccessUrl("/user")
                .permitAll()
                .and()

                .cors().configurationSource(corsConfigurationSource())
//                .formLogin()

        //登录页面 不设限访问
//                .loginPage("/login.html")
        //拦截的请求
//                .loginProcessingUrl("/login")
//                .successHandler(authenticationSuccessHandler).permitAll()
        ;

        // 添加JWT过滤器


        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//        httpSecurity.addFilterBefore(customCorsFilter, JwtRequestFilter.class);
    }
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("*"));
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfiguration);
        return source;
    }
    private class TrustedDomainsFilter extends OncePerRequestFilter {

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                        FilterChain filterChain) throws ServletException, IOException {
            // TODO Auto-generated method stub
            String domain = request.getHeader("Origin");
            System.out.println("域名是"+domain);
            if(domain==null) {
                System.out.println("同源");
            }
            if(domain!=null && domain.matches("^http://localhost(:\\\\d+)?$")) {
                System.out.println("域名对了");
            }else {
                System.out.println("域名不对");
                throw new ServletException("域名不对");
            }
            filterChain.doFilter(request, response);
//          return ;
        }
//    	@Override
//        public boolean matches(HttpServletRequest request) {
//            String domain = request.getHeader("Origin");
//            return domain != null && domain.matches("^http://localhost(:\\\\d+)?$");
//        }
    }
    /*// 验证JWT
    public Boolean validateToken(String token, UserDetails userDetails, String trustedDomain) {
        return jwtUtil.validateToken(token, userDetails, trustedDomain);
    }

    // 生成JWT
    public String generateToken(UserDetails userDetails, String trustedDomain) {
        return jwtUtil.generateToken(userDetails, trustedDomain);
    }

    // 创建Authentication对象
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(jwtUtil.extractUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPointBean() throws Exception {
        // 将 JwtAuthenticationEntryPoint 类暴露为 Spring Bean，以便在其他地方使用
        return new JwtAuthenticationEntryPoint();
    }*/
}

