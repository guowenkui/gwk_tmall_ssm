package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Product;

import java.util.List;

public interface IProductService {

    List<Product> list(int cid);
    Product get(int id);
    void add(Product product);
    void delete(int id);
    void update(Product product);
}
