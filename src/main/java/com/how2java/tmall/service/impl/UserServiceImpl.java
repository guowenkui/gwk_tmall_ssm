package com.how2java.tmall.service.impl;

import com.how2java.tmall.mapper.UserMapper;
import com.how2java.tmall.pojo.User;
import com.how2java.tmall.pojo.UserExample;
import com.how2java.tmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {


    @Autowired
    private UserMapper userMapper;


    @Override
    public List<User> list() {
        UserExample example = new UserExample();
        example.setOrderByClause("id desc");
        return this.userMapper.selectByExample(example);
    }

    @Override
    public User get(int id) {
        return this.userMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(User user) {
        this.userMapper.insert(user);
    }

    @Override
    public void delete(int id) {
        this.userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(User user) {
        this.userMapper.updateByPrimaryKeySelective(user);
    }
}