package com.how2java.tmall.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.service.ICategoryService;
import com.how2java.tmall.service.IProductService;
import com.how2java.tmall.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class ProductController {


    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IProductService productService;




    /**
     * 导向到一个分类的产品管理界面
     */
    @RequestMapping("admin_product_list")
    public String list(Model model, int cid, Page page){

        Category c = this.categoryService.get(cid);

        PageHelper.offsetPage(page.getStart(),page.getCount());
        List<Product> ps = this.productService.list(cid);

        int total = (int) new PageInfo<>(ps).getTotal();
        page.setTotal(total);
        page.setParam("&cid="+c.getId());

        model.addAttribute("page",page);
        model.addAttribute("c",c);
        model.addAttribute("ps",ps);
        return "admin/listProduct";
    }


    @RequestMapping("admin_product_add")
    public String add(Product product){
        this.productService.add(product);
        return "redirect:/admin_product_list?cid="+product.getCid();
    }


    @RequestMapping("admin_product_delete")
    public String delete(int id){
        Product product = this.productService.get(id);
        this.productService.delete(id);
        return "redirect:/admin_product_list?cid="+product.getCid();
    }


    /**
     * 导向到产品编辑界面
     */
    @RequestMapping("admin_product_edit")
    public String edit(Model model, int id){
        Product product = this.productService.get(id);
        model.addAttribute("p",product);
        return "admin/editProduct";
    }

    @RequestMapping("admin_product_update")
    public String update(Product product){
        this.productService.update(product);
        return "redirect:/admin_product_list?cid="+product.getCid();
    }


}
