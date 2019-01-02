package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Order;
import com.how2java.tmall.pojo.OrderItem;

import java.util.List;

public interface IOrderItemService {


    List<OrderItem> list();
    OrderItem get(int id);

    void add(OrderItem orderItem);
    void delete(int id);
    void update(OrderItem orderItem);

    void fill(List<Order> os);
    void fill(Order order);

    int getSaleCount(Integer productId);


    List<OrderItem> listByUser(int userId);

}
