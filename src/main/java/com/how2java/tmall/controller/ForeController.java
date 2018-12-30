package com.how2java.tmall.controller;


import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.service.ICategoryService;
import com.how2java.tmall.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class ForeController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IProductService productService;




    @RequestMapping("forehome")
    public String home(Model model){
        List<Category> cs = this.categoryService.list();
        this.productService.fill(cs);
        this.productService.fillByRow(cs);
        model.addAttribute("cs",cs);
        return "fore/home";
    }
}
