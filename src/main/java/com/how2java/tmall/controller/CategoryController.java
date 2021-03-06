package com.how2java.tmall.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.service.ICategoryService;
import com.how2java.tmall.util.ImageUtil;
import com.how2java.tmall.util.Page;
import com.how2java.tmall.util.UploadedImageFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    /**
     * 导向到天猫后台--分类管理界面
     * @param model
     * @param page
     * @return
     */
    @RequestMapping("admin_category_list")
    public  String list(Model model, Page page){
        PageHelper.offsetPage(page.getStart(),page.getCount());
        List<Category> cs = this.categoryService.list();
        int total = (int)new PageInfo<>(cs).getTotal();
        page.setTotal(total);
        model.addAttribute("page",page);
        model.addAttribute("cs",cs);
        return "include/admin/listCategory";
    }

    @RequestMapping("admin_category_add")
    public String add(Category category, HttpSession session, UploadedImageFile uploadedImageFile) throws IOException {
        this.categoryService.add(category);
        File imageFolder = new File(session.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder,category.getId()+".jpg");
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        uploadedImageFile.getImage().transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img,"jpg",file);
        return "redirect:/admin_category_list";
    }


    @RequestMapping("admin_category_delete")
    public String delete(int id,HttpSession session){
        this.categoryService.delete(id);
        File imageFolder = new File(session.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder,id+".jpg");
        file.delete();
        return "redirect:/admin_category_list";
    }

    /**
     * 导向到一个分类的编辑界面
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("admin_category_edit")
    public String edit(Model model,int id){
        Category c = this.categoryService.get(id);
        model.addAttribute("c",c);
        return "include/admin/editCategory";
    }

    @RequestMapping("admin_category_update")
    public String update(Category category,HttpSession session,UploadedImageFile uploadedImageFile) throws IOException {
        this.categoryService.update(category);
        MultipartFile image = uploadedImageFile.getImage();
        if (null!=image&&!image.isEmpty()){
            File imageFolder = new File(session.getServletContext().getRealPath("img/Category"));
            File file = new File(imageFolder,category.getId()+".jpg");
            image.transferTo(file);
            BufferedImage img = ImageUtil.change2jpg(file);
            ImageIO.write(img,"jpg",file);
        }
        return "redirect:/admin_category_list";
    }
}
