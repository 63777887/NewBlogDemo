package com.example.myblog.service;

import com.example.myblog.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

public class UserDetailsImpl implements UserDetails {

    private User user;
    private static final long serialVersionUID = 1L;
    public UserDetailsImpl(User user) {
        this.user=user;
    }

//    @Override
//    public boolean equals(Object obj) {
//        if (obj instanceof User){
//            return this.user.getName().equals(((User) obj).getName());
//        }
//        return false;
//    }
//
//    @Override
//    public int hashCode() {
//        return this.user.getName().hashCode();
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        UserDetailsImpl o1 = (UserDetailsImpl) o;
        return this.user.getName().equals(o1.getUsername());
    }

    @Override
    public int hashCode() {
        return user.getName().hashCode();
    }

    @Override
    public String toString() {
        return "UserDetailsImpl{" +
                "user=" + user +
                '}';
    }
}
