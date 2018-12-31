package com.how2java.tmall.service.impl;

import com.how2java.tmall.mapper.ReviewMapper;
import com.how2java.tmall.pojo.Review;
import com.how2java.tmall.pojo.ReviewExample;
import com.how2java.tmall.pojo.User;
import com.how2java.tmall.service.IReviewService;
import com.how2java.tmall.service.IUserService;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.util.List;

@Service
public class ReViewServiceImpl implements IReviewService {


    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private IUserService userService;


    @Override
    public List<Review> list(int productId) {
        ReviewExample example = new ReviewExample();
        example.setOrderByClause("id desc");
        example.createCriteria().andPidEqualTo(productId);
        List<Review> reviews = this.reviewMapper.selectByExample(example);
        setUser(reviews);
        return reviews;
    }

    @Override
    public Review get(int id) {
        Review review = this.reviewMapper.selectByPrimaryKey(id);
        setUser(review);
        return review;
    }

    @Override
    public void add(Review review) {
        this.reviewMapper.insert(review);
    }

    @Override
    public void delete(int id) {
        this.reviewMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Review review) {
        this.reviewMapper.updateByPrimaryKeySelective(review);
    }

    @Override
    public int getReviewCount(int productId) {
        return list(productId).size();
    }

    public void setUser(Review review){
        User user = this.userService.get(review.getUid());
        review.setUser(user);
    }

    public void setUser(List<Review> list){
        for (Review review:list){
            setUser(review);
        }
    }
}
