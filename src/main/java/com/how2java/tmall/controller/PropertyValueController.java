package com.how2java.tmall.controller;


import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.Property;
import com.how2java.tmall.pojo.PropertyValue;
import com.how2java.tmall.service.IProductService;
import com.how2java.tmall.service.IPropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class PropertyValueController {

    @Autowired
    private IPropertyValueService propertyValueService;

    @Autowired
    private IProductService productService;


    /**
     * 导向到一个产品的设置属性界面
     * @return
     */
    @RequestMapping("admin_propertyValue_edit")
    private String list(Model model,int pid){
        Product p = this.productService.get(pid);
        this.propertyValueService.init(p);
        List<PropertyValue> pvs = this.propertyValueService.list(pid);
        model.addAttribute("p",p);
        model.addAttribute("pvs",pvs);
        return  "admin/editPropertyValue";
    }

    @RequestMapping("admin_propertyValue_update")
    @ResponseBody
    public String update(PropertyValue propertyValue){
        this.propertyValueService.update(propertyValue);
        return "success";
    }


}
