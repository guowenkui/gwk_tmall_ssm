package com.how2java.tmall.service.impl;

import com.how2java.tmall.mapper.OrderItemMapper;
import com.how2java.tmall.mapper.OrderMapper;
import com.how2java.tmall.pojo.Order;
import com.how2java.tmall.pojo.OrderItem;
import com.how2java.tmall.pojo.OrderItemExample;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.service.IOrderItemService;
import com.how2java.tmall.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.List;

@Service
public class OrderItemServiceImpl implements IOrderItemService {


    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private IProductService productService;


    @Override
    public List<OrderItem> list() {
        OrderItemExample example = new OrderItemExample();
        example.setOrderByClause("id desc");
        return this.orderItemMapper.selectByExample(example);
    }

    @Override
    public OrderItem get(int id) {
        return this.orderItemMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(OrderItem orderItem) {
        this.orderItemMapper.insert(orderItem);
    }

    @Override
    public void delete(int id) {
        this.orderItemMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(OrderItem orderItem) {
        this.orderItemMapper.updateByPrimaryKeySelective(orderItem);
    }


    @Override
    public void fill(List<Order> os) {
        for (Order order:os){
            fill(order);
        }
    }

    @Override
    public void fill(Order order) {
        OrderItemExample example = new OrderItemExample();
        example.createCriteria().andOidEqualTo(order.getId());
        example.setOrderByClause("id desc");
        List<OrderItem> list = this.orderItemMapper.selectByExample(example);
        setProduct(list);

        float total = 0;
        int totalNumber = 0;
        for (OrderItem orderItem : list){
            total+=orderItem.getNumber()*orderItem.getProduct().getPromotePrice();
            totalNumber+=orderItem.getNumber();
        }

        order.setTotal(total);
        order.setTotalNumber(totalNumber);
        order.setOrderItems(list);

    }

    public void setProduct(List<OrderItem> ois){
        for (OrderItem orderItem:ois){
            setProduct(orderItem);
        }
    }

    private void setProduct(OrderItem orderItem){
        Product product = this.productService.get(orderItem.getPid());
        orderItem.setProduct(product);
    }


    @Override
    public int getSaleCount(Integer productId) {
        OrderItemExample example = new OrderItemExample();
        example.createCriteria().andPidEqualTo(productId);
        List<OrderItem> list = this.orderItemMapper.selectByExample(example);
        int number=0;
        for (OrderItem item : list){
            number+=item.getNumber();
        }
        return number;
    }


}
