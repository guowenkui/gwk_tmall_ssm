package com.how2java.tmall.service.impl;

import com.how2java.tmall.mapper.ProductMapper;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.ProductExample;
import com.how2java.tmall.service.ICategoryService;
import com.how2java.tmall.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ICategoryService categoryService;



    @Override
    public List<Product> list(int cid) {

        ProductExample example = new ProductExample();
        example.setOrderByClause("id desc");
        example.createCriteria().andCidEqualTo(cid);
        List<Product> list = this.productMapper.selectByExample(example);
        setCategory(list);
        return list;
    }

    @Override
    public Product get(int id) {
        Product product = this.productMapper.selectByPrimaryKey(id);
        setCategory(product);
        return product;
    }

    @Override
    public void add(Product product) {
        this.productMapper.insert(product);
    }

    @Override
    public void delete(int id) {
        this.productMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Product product) {
        this.productMapper.updateByPrimaryKeySelective(product);
    }



    public void setCategory(List<Product> list){
        for(Product p:list){
            setCategory(p);
        }
    }

    public void setCategory(Product p){
        int cid = p.getCid();
        Category c = this.categoryService.get(cid);
        p.setCategory(c);
    }

}
