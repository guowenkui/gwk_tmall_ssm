package com.how2java.tmall.service.impl;

import com.how2java.tmall.mapper.ProductImageMapper;
import com.how2java.tmall.pojo.ProductImage;
import com.how2java.tmall.pojo.ProductImageExample;
import com.how2java.tmall.service.IProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductImageServiceImpl implements IProductImageService {


    @Autowired
    private ProductImageMapper productImageMapper;



    @Override
    public List<ProductImage> list(int pid, String type) {
        ProductImageExample example = new ProductImageExample();
        example.setOrderByClause("id desc");
        example.createCriteria()
                .andPidEqualTo(pid)
                .andTypeEqualTo(type);
        return this.productImageMapper.selectByExample(example);
    }

    @Override
    public ProductImage get(int id) {
        return this.productImageMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(ProductImage productImage) {
        this.productImageMapper.insert(productImage);
    }

    @Override
    public void delete(int id) {
        this.productImageMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(ProductImage productImage) {
        this.productImageMapper.updateByPrimaryKeySelective(productImage);
    }
}
