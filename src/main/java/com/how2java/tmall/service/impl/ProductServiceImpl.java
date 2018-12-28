package com.how2java.tmall.service.impl;

import com.how2java.tmall.mapper.ProductMapper;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.ProductExample;
import com.how2java.tmall.pojo.ProductImage;
import com.how2java.tmall.service.ICategoryService;
import com.how2java.tmall.service.IProductImageService;
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

    @Autowired
    private IProductImageService productImageService;



    @Override
    public List<Product> list(int cid) {

        ProductExample example = new ProductExample();
        example.setOrderByClause("id desc");
        example.createCriteria().andCidEqualTo(cid);
        List<Product> list = this.productMapper.selectByExample(example);
        setCategory(list);
        setFirstProductImage(list);
        return list;
    }

    @Override
    public Product get(int id) {
        Product product = this.productMapper.selectByPrimaryKey(id);
        setCategory(product);
        setFirstProductImage(product);
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

    public void setFirstProductImage(List<Product> list){
        for (Product product:list){
            setFirstProductImage(product);
        }
    }


    public void setFirstProductImage(Product p){
        List<ProductImage> list = this.productImageService.list(p.getId(),IProductImageService.type_single);
        if (!list.isEmpty()){
            ProductImage productImage = list.get(0);
            p.setFirstProductImage(productImage);
        }
    }


}
