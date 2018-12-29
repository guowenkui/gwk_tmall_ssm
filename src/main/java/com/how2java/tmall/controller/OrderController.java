package com.how2java.tmall.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.how2java.tmall.pojo.Order;
import com.how2java.tmall.service.IOrderItemService;
import com.how2java.tmall.service.IOrderService;
import com.how2java.tmall.util.Page;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

@Controller
public class OrderController {


    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderItemService orderItemService;


    /**
     * 导向到用户订单管理界面
     */

    @RequestMapping("admin_order_list")
    public String list(Model model, Page page){
        PageHelper.offsetPage(page.getStart(),page.getCount());
        List<Order> os = this.orderService.list();
        int total = (int) new PageInfo<>(os).getTotal();
        this.orderItemService.fill(os);
        page.setTotal(total);
        model.addAttribute("page",page);
        model.addAttribute("os",os);
        return "admin/listOrder";
    }


    @RequestMapping("admin_order_delivery")
    public String delivery(Order order){
        order.setDeliveryDate(new Date());
        order.setStatus(IOrderService.waitConfirm);
        this.orderService.update(order);
        return "redirect:admin_order_list";
    }
}
