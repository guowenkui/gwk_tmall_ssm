package com.how2java.tmall.controller;


import com.how2java.tmall.pojo.*;
import com.how2java.tmall.service.*;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
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
}
