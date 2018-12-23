package com.how2java.tmall.service.impl;

import com.how2java.tmall.mapper.CategoryMapper;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.service.CategoryService;
import com.how2java.tmall.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public int total() {
        return this.categoryMapper.total();
    }

    @Override
        public List<Category> list(Page page) {
            return this.categoryMapper.list(page);
    }

    @Override
    public void add(Category category) {
        this.categoryMapper.add(category);
    }

    @Override
    public void delete(Long id) {
        this.categoryMapper.delete(id);
    }
}
