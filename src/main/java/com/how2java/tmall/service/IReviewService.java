package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Review;

import java.util.List;

public interface IReviewService {

    List<Review> list(int productId);
    Review get(int id);

    void add(Review review);
    void delete(int id);
    void update(Review review);

    int getReviewCount(int productId);
}
