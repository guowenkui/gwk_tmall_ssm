package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Order;

import java.util.List;

public interface IOrderService {


    String waitPay = "waitPay";
    String waitDelivery = "waitDelivery";
    String waitConfirm = "waitConfirm";
    String waitReview = "waitReview";
    String finish = "finish";
    String delete = "delete";


    List<Order> list();
    Order get(int id);

    void add(Order order);
    void delete(int id);
    void update(Order order);
}
