package com.how2java.tmall.service.impl;

import com.how2java.tmall.mapper.ProductMapper;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.ProductExample;
import com.how2java.tmall.pojo.ProductImage;
import com.how2java.tmall.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IProductImageService productImageService;

    @Autowired
    private IOrderItemService orderItemService;

    @Autowired
    private IReviewService reviewService;



    @Override
    public List<Product> list(int cid) {

        ProductExample example = new ProductExample();
        example.setOrderByClause("id desc");
        example.createCriteria().andCidEqualTo(cid);
        List<Product> list = this.productMapper.selectByExample(example);
        setCategory(list);
        setFirstProductImage(list);
        setProductSingleImages(list);
        setProductDetailImages(list);
        return list;
    }

    @Override
    public Product get(int id) {
        Product product = this.productMapper.selectByPrimaryKey(id);
        setCategory(product);
        setFirstProductImage(product);
        setProductSingleImages(product);
        setProductDetailImages(product);
        return product;
    }

    @Override
    public void add(Product product) {
        this.productMapper.insert(product);
    }

    @Override
    public void delete(int id) {
        this.productMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Product product) {
        this.productMapper.updateByPrimaryKeySelective(product);
    }

    @Override
    public void fill(List<Category> cs) {
        for (Category category:cs){
            fill(category);
        }
    }

    @Override
    public void fill(Category c) {
        List<Product> ps = this.list(c.getId());
        c.setProducts(ps);
    }

    @Override
    public void fillByRow(List<Category> cs) {
        int productNumberEachRow = 8;
        for (Category category:cs){
            List<Product> products = category.getProducts();
            List<List<Product>> productsByRow = new ArrayList<>();

            for (int i=0;i<products.size();i+=productNumberEachRow){
                int size = i+productNumberEachRow;
                size = size>products.size()?products.size():size;
                List<Product> productsOfEachRow = products.subList(i,size);
                productsByRow.add(productsOfEachRow);
            }
            category.setProductsByRow(productsByRow);
        }
    }

    @Override
    public void setSaleAndReviewNumber(Product product) {
        int saleCount = this.orderItemService.getSaleCount(product.getId());
        product.setSaleCount(saleCount);

        int reviewCount = this.reviewService.getReviewCount(product.getCid());
        product.setReviewCount(reviewCount);
    }

    @Override
    public void setSaleAndReviewNumber(List<Product> list) {
        for (Product product:list){
            setSaleAndReviewNumber(product);
        }
    }


    public void setCategory(List<Product> list){
        for(Product p:list){
            setCategory(p);
        }
    }

    public void setCategory(Product p){
        int cid = p.getCid();
        Category c = this.categoryService.get(cid);
        p.setCategory(c);
    }

    public void setFirstProductImage(List<Product> list){
        for (Product product:list){
            setFirstProductImage(product);
        }
    }


    public void setFirstProductImage(Product p){
        List<ProductImage> list = this.productImageService.list(p.getId(),IProductImageService.type_single);
        if (!list.isEmpty()){
            ProductImage productImage = list.get(0);
            p.setFirstProductImage(productImage);
        }
    }

    public void setProductSingleImages(List<Product> list){
        for (Product product:list){
            setProductSingleImages(product);
        }
    }

    public void setProductSingleImages(Product p){
        List<ProductImage> list = this.productImageService.list(p.getId(),IProductImageService.type_single);
        if (!list.isEmpty()){
            p.setProductSingleImages(list);
        }
    }


    public void setProductDetailImages(List<Product> products){
        for (Product product:products){
            setProductDetailImages(product);
        }
    }
    public void setProductDetailImages(Product product){
        List<ProductImage> list = this.productImageService.list(product.getId(),IProductImageService.type_detail);
        if (!list.isEmpty()){
            product.setProductDetailImages(list);
        }
    }


}
