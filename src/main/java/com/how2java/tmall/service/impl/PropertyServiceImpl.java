package com.how2java.tmall.service.impl;

import com.how2java.tmall.mapper.PropertyMapper;
import com.how2java.tmall.pojo.Property;
import com.how2java.tmall.pojo.PropertyExample;
import com.how2java.tmall.service.IPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyServiceImpl implements IPropertyService {


    @Autowired
    private PropertyMapper propertyMapper;



    @Override
    public List<Property> list(int cid) {
        PropertyExample propertyExample = new PropertyExample();
        propertyExample.createCriteria().andCidEqualTo(cid);
        propertyExample.setOrderByClause("id desc");
        return this.propertyMapper.selectByExample(propertyExample);
    }

    @Override
    public Property get(int id) {
        return this.propertyMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(Property property) {
        this.propertyMapper.insert(property);
    }

    @Override
    public void delete(int id) {
        this.propertyMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Property property) {
        this.propertyMapper.updateByPrimaryKeySelective(property);
    }
}
