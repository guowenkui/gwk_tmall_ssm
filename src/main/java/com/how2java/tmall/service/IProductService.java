package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Product;

import java.util.List;

public interface IProductService {

    List<Product> list(int cid);
    Product get(int id);
    void add(Product product);
    void delete(int id);
    void update(Product product);


    void fill(List<Category> cs);
    void fill(Category c);
    void fillByRow(List<Category> cs);

    void setSaleAndReviewNumber(Product product);
    void setSaleAndReviewNumber(List<Product> list);
}
