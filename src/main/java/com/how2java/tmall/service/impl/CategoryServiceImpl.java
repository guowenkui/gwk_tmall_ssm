package com.how2java.tmall.service.impl;

import com.how2java.tmall.mapper.CategoryMapper;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.CategoryExample;
import com.how2java.tmall.service.CategoryService;
import com.how2java.tmall.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> list() {
        CategoryExample example = new CategoryExample();
        example.setOrderByClause("id desc");
        return this.categoryMapper.selectByExample(example);
    }

    @Override
    public void add(Category category) {
        this.categoryMapper.insert(category);
    }

    @Override
    public void delete(Long id) {
        this.categoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Category get(Long id) {
        return this.categoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(Category category) {
        this.categoryMapper.updateByPrimaryKeySelective(category);
    }
}
