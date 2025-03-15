package com.openai.code.Service;

import com.openai.code.Entity.CodeReview;

import java.util.List;

public interface CodeReviewService {

    CodeReview analyzeCode(String code, String lang);
    CodeReview getReviewById(Long id);
    List<CodeReview> getAllReviews();
}
