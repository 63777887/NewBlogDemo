package com.example.myblog.config;

import com.example.myblog.component.CaptchaCodeFilter;
import com.example.myblog.component.MyAuthenticationFailureHandler;
import com.example.myblog.component.MyAuthenticationSuccessHandler;
import com.example.myblog.component.MyExpiredSessionStrategy;
import com.example.myblog.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class MySecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    @Qualifier("db")
    private MyUserDetailService myUserDetailService;

//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        // need userDetailService
//        provider.setUserDetailsService(myUserDetailService);
//        // encoder
//        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
//        return provider;
//    }


    @Bean
    public PasswordEncoder mypasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailService).passwordEncoder(mypasswordEncoder());

//        auth.inMemoryAuthentication().
//                withUser("user")
//                .password("123")
////                .roles("admin")   //用户权限
//                .authorities("adminid:admin");  //权限id
    }

    @Autowired
    MyAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    MyAuthenticationFailureHandler authenticationFailureHandler;

    @Resource
    CaptchaCodeFilter captchaCodeFilter;



    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.rememberMe().rememberMeParameter("remember-me-new")    //设置ajax传参的名字，必须要与ajax一致
//                .rememberMeCookieName("fjsleiurio")     //设置remember名字
//                .tokenValiditySeconds(60*60*24)     //记住密码一天
//                .and().formLogin().loginPage("/login").successHandler(authenticationSuccessHandler)
//                .failureHandler(authenticationFailureHandler)
//                .and()
//                .authorizeRequests()
////                .antMatchers("/admin").hasRole("ROLE_putong")
////                .antMatchers("/admin/**")
//////                .hasRole("admin")     //用户权限
//////                .hasAnyAuthority("ROLE_yonghu")   //用户权限
////                .hasAuthority("adminid:admin")      //资源id权限
//                .anyRequest().permitAll()
//        .and().sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)   //session是否生成(IF_REQUIRED是在需要时创建)
//                .invalidSessionUrl("/login")    //session过期以后跳转登陆页
//            .sessionFixation().migrateSession()     //管理session的存活方式
//                .maximumSessions(1)         //只允许一个session出现
//                .maxSessionsPreventsLogin(false)    //允许其他人登陆(只允许一个登陆，会挤掉当前)
//        .expiredSessionStrategy(new MyExpiredSessionStrategy());


        http.addFilterBefore(captchaCodeFilter,UsernamePasswordAuthenticationFilter.class)
                .formLogin().loginPage("/login").usernameParameter("username")
                .passwordParameter("password")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
        .and()
             .csrf().disable().authorizeRequests()
                .antMatchers("/login", "/kaptcha","/search",".index").permitAll()
                .antMatchers("/admin/**").authenticated()
                .anyRequest().permitAll()
        .and()
            .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutUrl("/logout")
                .logoutSuccessUrl("/").permitAll()
        .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)   //session是否生成(IF_REQUIRED是在需要时创建)
                .invalidSessionUrl("/login")    //session过期以后跳转登陆页
                .sessionFixation().migrateSession()     //管理session的存活方式
                .maximumSessions(1)         //只允许一个session出现
                .maxSessionsPreventsLogin(false)    //允许其他人登陆(只允许一个登陆，会挤掉当前)
                .expiredSessionStrategy(new MyExpiredSessionStrategy());
    }
}