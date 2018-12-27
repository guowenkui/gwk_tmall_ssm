package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Property;

import java.util.List;

public interface IPropertyService {

    List<Property> list(int cid);
    Property get(int id);
    void  add(Property property);
    void  delete(int id);
    void  update(Property property);
}
