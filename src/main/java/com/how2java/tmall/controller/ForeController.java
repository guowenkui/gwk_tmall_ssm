package com.how2java.tmall.controller;


import com.github.pagehelper.PageHelper;
import com.how2java.tmall.pojo.*;
import com.how2java.tmall.service.*;
import com.sun.org.apache.xpath.internal.operations.Mod;
import comparator.*;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ForeController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IReviewService reviewService;

    @Autowired
    private IPropertyValueService propertyValueService;

    @Autowired
    private IOrderItemService orderItemService;

    @Autowired
    private IOrderService orderService;





    @RequestMapping("forehome")
    public String home(Model model){
        List<Category> cs = this.categoryService.list();
        this.productService.fill(cs);
        this.productService.fillByRow(cs);
        model.addAttribute("cs",cs);
        return "fore/home";
    }

    @RequestMapping("foreregister")
    public String register(Model model, User user){
        String name = user.getName();
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);
        boolean exist = this.userService.isExist(name);

        if (exist){
            String m = "用户名已经被使用,不能使用";
            model.addAttribute("msg",m);
            model.addAttribute("user",null);
            return "fore/register";
        }
        this.userService.add(user);

        return "redirect:registerSuccessPage";
    }


    @RequestMapping("forelogin")
    public String login(@RequestParam("name") String name, @RequestParam("password") String password, Model model, HttpSession session){

        name = HtmlUtils.htmlEscape(name);
        User user = this.userService.get(name,password);
        if (user==null){
            model.addAttribute("msg","账号密码错误");
            return "fore/login";
        }
        session.setAttribute("user",user);
        return "redirect:forehome";
    }

    @RequestMapping("forelogout")
    public String logout(HttpSession session){

        session.removeAttribute("user");
        return "redirect:forehome";
    }


    @RequestMapping("foreproduct")
    public String product(int pid,Model model){

        Product product = this.productService.get(pid);
        List<Review> reviews = this.reviewService.list(pid);
        List<PropertyValue> pvs = this.propertyValueService.list(pid);

        this.productService.setSaleAndReviewNumber(product);

        model.addAttribute("p",product);
        model.addAttribute("reviews",reviews);
        model.addAttribute("pvs",pvs);
        return "fore/product";
    }

    @RequestMapping("forecheckLogin")
    @ResponseBody
    public String checkLogin(HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user!=null){
            return "success";
        }else {
            return "fail";
        }
    }

    @RequestMapping("foreloginAjax")
    @ResponseBody
    public String loginAjax(@RequestParam("name")String name,@RequestParam("password") String password,HttpSession session){
        name = HtmlUtils.htmlEscape(name);
        User user = this.userService.get(name,password);
        if (user!=null){
            session.setAttribute("user",user);
            return "success";
        }else {
            return "fail";
        }
    }

    @RequestMapping("forecategory")
    public String foreCategory(Model model,String sort,int cid){
        Category category = this.categoryService.get(cid);
        this.productService.fill(category);
        this.productService.setSaleAndReviewNumber(category.getProducts());
        if (sort!=null){
            switch (sort){
                case "all":
                    Collections.sort(category.getProducts(),new ProductAllComparator());
                    break;
                case "date":
                    Collections.sort(category.getProducts(),new ProductDateComparator());
                    break;
                case "review":
                    Collections.sort(category.getProducts(),new ProductReviewComparator());
                    break;
                case "price":
                    Collections.sort(category.getProducts(),new ProductPriceComparator());
                    break;
                case "saleCount":
                    Collections.sort(category.getProducts(),new ProductSaleCountComparator());
                    break;
            }
        }
        model.addAttribute("c",category);
        return "fore/category";
    }

    @RequestMapping("foresearch")
    public String search(Model model,String keyword){
        PageHelper.offsetPage(0,20);
        List<Product> list = this.productService.search(keyword);
        this.productService.setSaleAndReviewNumber(list);
        model.addAttribute("ps",list);
        return "fore/searchResult";
    }


    @RequestMapping("forebuyone")
    public String buyone(int pid,int num,HttpSession session){
        Product product = this.productService.get(pid);
        int oiid = 0;

        User user = (User) session.getAttribute("user");
        boolean found = false;
        List<OrderItem> list = this.orderItemService.listByUser(user.getId());
        for (OrderItem item:list){
            if (item.getProduct().getId().intValue() ==product.getId().intValue()){
                item.setNumber(item.getNumber()+num);
                this.orderItemService.update(item);
                found  =true;
                oiid = item.getId();
                break;
            }
        }

        if (!found){
            OrderItem item = new OrderItem();
            item.setUid(user.getId());
            item.setNumber(num);
            item.setPid(pid);
            this.orderItemService.add(item);
            oiid = item.getId();
        }
        return "redirect:forebuy?oiid="+oiid;
    }

    @RequestMapping("forebuy")
    public String buy(Model model,String[] oiid,HttpSession session){

        List<OrderItem> ois = new ArrayList<>();
        float total = 0;
        for (String strid:oiid){
            int id = Integer.parseInt(strid);
            OrderItem orderItem = this.orderItemService.get(id);
            total +=orderItem.getProduct().getPromotePrice()*orderItem.getNumber();
            ois.add(orderItem);
        }
        session.setAttribute("ois",ois);
        model.addAttribute("total",total);
        return "fore/buy";
    }

    @RequestMapping("foreaddCart")
    @ResponseBody
    public String addCart(Model model,int pid,int num,HttpSession session){
        Product product = this.productService.get(pid);
        User user = (User) session.getAttribute("user");
        boolean found = false;

        List<OrderItem> list = this.orderItemService.listByUser(user.getId());
        for (OrderItem item:list){
            if (item.getProduct().getId().intValue()==product.getId().intValue()){
                item.setNumber(item.getNumber()+num);
                this.orderItemService.update(item);
                found = true;
                break;
            }
        }

        if (!found){
            OrderItem item = new OrderItem();
            item.setNumber(num);
            item.setPid(pid);
            item.setUid(user.getId());
            this.orderItemService.add(item);
        }

        return "success";
    }

    @RequestMapping("forecart")
    public String cart(Model model,HttpSession session){
        User user = (User) session.getAttribute("user");
        List<OrderItem> list = this.orderItemService.listByUser(user.getId());
        model.addAttribute("ois",list);
        return "fore/cart";
    }

    /**
     * 购物车操作--删除
     */
    @RequestMapping("foredeleteOrderItem")
    @ResponseBody
    public String deleteOrderItem(int oiid,HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user!=null){
            return "fail";
        }
        this.orderItemService.delete(oiid);
        return "success";
    }


    /**
     * 购物车操作--改变订单数量
     */
    @RequestMapping("forechangeOrderItem")
    @ResponseBody
    public String changeOrderItem(int pid,int number,HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user==null){
            return "fail";
        }
        List<OrderItem> list = this.orderItemService.listByUser(user.getId());
        for (OrderItem item:list){
            if (item.getProduct().getId().intValue()==pid){
                item.setNumber(number);
                this.orderItemService.update(item);
                break;
            }
        }
        return "success";
    }


    /**
     *提交订单
     */
    @RequestMapping("forecreateOrder")
    public String createOrder(Order order,HttpSession session){
        User user = (User) session.getAttribute("user");
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+ RandomUtils.nextInt(10000);

        order.setOrderCode(orderCode);
        order.setCreateDate(new Date());
        order.setStatus(IOrderService.waitPay);
        order.setUid(user.getId());

        List<OrderItem> orderItemList = (List<OrderItem>) session.getAttribute("ois");
        float total = this.orderService.add(order,orderItemList);
        return "redirect:forealipay?oid="+order.getId()+"&total="+total;
    }


    /**
     *
     */
    @RequestMapping("forepayed")
    public String payed(Model model,int oid,float total){
        Order order = this.orderService.get(oid);
        order.setPayDate(new Date());
        order.setStatus(IOrderService.waitDelivery);
        this.orderService.update(order);
        
        model.addAttribute("o",order);
        return "fore/payed";
    }

}
