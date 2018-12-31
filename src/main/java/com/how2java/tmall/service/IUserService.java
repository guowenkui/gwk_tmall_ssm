package com.how2java.tmall.service;

import com.how2java.tmall.pojo.User;

import java.util.List;

public interface IUserService {

    List<User> list();
    User get(int id);

    void add(User user);
    void delete(int id);
    void update(User user);

    boolean isExist(String name);
    User get(String username,String password);
}
