package com.example.myblog.bean;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Data
//@Entity     //持久化
public class User implements  UserDetails {
//public class User {
    private Integer id;
    private String name;
    private String password;
    private String email;
    private String avatar;




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return name;
    }
    @Override
    public String getPassword() {
        return password;
    }
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore //忽略不属于user成员的方法
    @Override
    public boolean isEnabled() {
        return true;
    }
}
