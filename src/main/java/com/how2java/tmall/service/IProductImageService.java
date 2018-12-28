package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.ProductImage;

import java.util.List;

public interface IProductImageService {

    String type_single = "type_single";
    String type_detail = "type_detail";

    List<ProductImage> list(int pid,String type);
    ProductImage get(int id);
    void add(ProductImage productImage);
    void delete(int id);
    void update(ProductImage productImage);
}
