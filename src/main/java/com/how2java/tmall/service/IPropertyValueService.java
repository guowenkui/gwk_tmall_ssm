package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.PropertyValue;

import java.util.List;

public interface IPropertyValueService {

    void  init(Product product);
    void  update(PropertyValue pv);

    PropertyValue get(int ptid,int pid);
    List<PropertyValue> list(int pid);

}
