package com.how2java.tmall.controller;


import com.github.pagehelper.PageHelper;
import com.how2java.tmall.pojo.*;
import com.how2java.tmall.service.*;
import com.sun.org.apache.xpath.internal.operations.Mod;
import comparator.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

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
}
