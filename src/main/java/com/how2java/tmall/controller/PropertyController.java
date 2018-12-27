package com.how2java.tmall.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Property;
import com.how2java.tmall.service.ICategoryService;
import com.how2java.tmall.service.IPropertyService;
import com.how2java.tmall.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("")
public class PropertyController {


    @Autowired
    private IPropertyService propertyService;

    @Autowired
    private ICategoryService categoryService;



    /**
     * 导向到一个分类的属性管理界面
     * @param model
     * @param cid
     * @param page
     * @return
     */
    @RequestMapping("admin_property_list")
    public  String list(Model model, int cid, Page page){

        Category category = this.categoryService.get(cid);

        PageHelper.offsetPage(page.getStart(),page.getCount());
        List<Property> ps = this.propertyService.list(cid);

        int total = (int) new PageInfo<>(ps).getTotal();
        page.setTotal(total);
        page.setParam("&cid="+ category.getId());

        model.addAttribute("page",page);
        model.addAttribute("ps",ps);
        model.addAttribute("c",category);

        return "include/admin/listProperty";
    }

    @RequestMapping("admin_property_add")
    public String add(Property p){
        this.propertyService.add(p);
        return "redirect:/admin_property_list?cid="+p.getCid();
    }

    @RequestMapping("admin_property_delete")
    public String delete(int id){
        Property p = this.propertyService.get(id);
        this.propertyService.delete(id);
        return "redirect:/admin_property_list?cid="+p.getCid();
    }

    /**
     * 导向到属性编辑界面
     */
    @RequestMapping("admin_property_edit")
    public String edit(Model model,int id){
        Property p = this.propertyService.get(id);
        Category c = this.categoryService.get(p.getCid());
        p.setCategory(c);
        model.addAttribute("p",p);
        return "include/admin/editProperty";
    }

    @RequestMapping("admin_property_update")
    public String update(Property p){
        this.propertyService.update(p);
        return "redirect:/admin_property_list?cid="+p.getCid();
    }


}
