package com.how2java.tmall.controller;


import com.alibaba.druid.sql.visitor.functions.If;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.ProductImage;
import com.how2java.tmall.service.IProductImageService;
import com.how2java.tmall.service.IProductService;
import com.how2java.tmall.util.ImageUtil;
import com.how2java.tmall.util.UploadedImageFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.java2d.pipe.BufferedBufImgOps;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class ProductImageController {


    @Autowired
    private IProductImageService productImageService;

    @Autowired
    private IProductService productService;


    /**
     * 导向到一个产品图片管理的界面
     */
    @RequestMapping("admin_productImage_list")
    public String list(Model model,int pid){
        Product p = this.productService.get(pid);
        List<ProductImage> pisSingle = this.productImageService.list(pid,IProductImageService.type_single);
        List<ProductImage> pisDetail = this.productImageService.list(pid,IProductImageService.type_detail);

        model.addAttribute("p",p);
        model.addAttribute("pisSingle",pisSingle);
        model.addAttribute("pisDetail",pisDetail);
        return "admin/listProductImage";
    }

    @RequestMapping("admin_productImage_delete")
    public String delete(int id, HttpSession session){
        ProductImage productImage = this.productImageService.get(id);

        String fileName = productImage.getId()+".jpg";
        String imageFolder;
        String imageFolder_middle = null;
        String imageFolder_small = null;


        if (IProductImageService.type_single.equals(productImage.getType())){
            imageFolder = session.getServletContext().getRealPath("img/productSingle");
            imageFolder_middle = session.getServletContext().getRealPath("img/productSingle_middle");
            imageFolder_small = session.getServletContext().getRealPath("img/productSingle_small");

            File imageFile = new File(imageFolder,fileName);
            File f_middle = new File(imageFolder_middle,fileName);
            File f_small = new File(imageFolder_small,fileName);

            imageFile.delete();
            f_middle.delete();
            f_small.delete();
        }else {
            imageFolder = session.getServletContext().getRealPath("img/productDetail");
            File imageDetail = new File(imageFolder,fileName);
            imageDetail.delete();
        }

        this.productImageService.delete(id);
        return "redirect:/admin_productImage_list?pid="+productImage.getPid();
    }


    @RequestMapping("admin_productImage_add")
    public String add(ProductImage productImage, HttpSession session, UploadedImageFile uploadedImageFile){
        this.productImageService.add(productImage);
        String fileName = productImage.getId()+".jpg";
        String imageFolder;
        String imageFolder_middle = null;
        String imageFolder_small = null;
        if (IProductImageService.type_single.equals(productImage.getType())){
            imageFolder = session.getServletContext().getRealPath("img/productSingle");
            imageFolder_middle = session.getServletContext().getRealPath("img/productSinge_middle");
            imageFolder_small = session.getServletContext().getRealPath("img/productSingle_small");
        }else {
            imageFolder = session.getServletContext().getRealPath("img/productDetail");
        }
        File f = new File(imageFolder,fileName);
        f.getParentFile().mkdirs();
        try {
            uploadedImageFile.getImage().transferTo(f);
            BufferedImage img = ImageUtil.change2jpg(f);
            ImageIO.write(img,"jpg",f);
            if (IProductImageService.type_single.equals(productImage.getType())){
                File f_middle = new File(imageFolder_middle,fileName);
                File f_small = new File(imageFolder_small,fileName);

                ImageUtil.resizeImage(f,56,56,f_small);
                ImageUtil.resizeImage(f,217,190,f_middle);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:admin_productImage_list?pid="+productImage.getPid();
    }


}
