package com.how2java.tmall.mapper;

import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.CategoryExample;
import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Category record);

    int insertSelective(Category record);

    List<Category> selectByExample(CategoryExample example);

    Category selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);
}