package com.how2java.tmall.service.impl;

import com.how2java.tmall.mapper.OrderMapper;
import com.how2java.tmall.pojo.Order;
import com.how2java.tmall.pojo.OrderExample;
import com.how2java.tmall.pojo.OrderItem;
import com.how2java.tmall.pojo.User;
import com.how2java.tmall.service.IOrderItemService;
import com.how2java.tmall.service.IOrderService;
import com.how2java.tmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private IUserService userService;

    @Autowired
    private IOrderItemService orderItemService;



    @Override
    public List<Order> list() {
        OrderExample example = new OrderExample();
        example.setOrderByClause("id desc");
        List<Order> list = this.orderMapper.selectByExample(example);
        setUser(list);
        return list;
    }

    @Override
    public Order get(int id) {
        return this.orderMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(Order order) {
        this.orderMapper.insert(order);
    }

    @Override
    public void delete(int id) {
        this.orderMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Order order) {
        this.orderMapper.updateByPrimaryKeySelective(order);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackForClassName = "Exception")
    public float add(Order order, List<OrderItem> orderItemList) {
        add(order);

        float total = 0;
        if (false){
            throw new RuntimeException();
        }
        for (OrderItem orderItem:orderItemList){
            orderItem.setOid(order.getId());
            this.orderItemService.update(orderItem);
            total+=orderItem.getProduct().getPromotePrice()*orderItem.getNumber();
        }
        return total;
    }

    @Override
    public List<Order> listByOrder(int uid,String excludedStatus) {
        OrderExample example = new OrderExample();
        example.createCriteria().andUidEqualTo(uid).andStatusNotEqualTo(excludedStatus);
        example.setOrderByClause("id desc");
        List<Order> list = this.orderMapper.selectByExample(example);
        return list;
    }


    public void setUser(List<Order> list){
        for (Order order:list){
            setUser(order);
        }
    }

    public void setUser(Order order){
        User user = this.userService.get(order.getUid());
        order.setUser(user);
    }
}
