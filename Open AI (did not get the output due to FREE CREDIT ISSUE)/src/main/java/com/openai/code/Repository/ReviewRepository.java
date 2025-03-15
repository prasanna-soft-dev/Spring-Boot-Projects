package com.openai.code.Repository;

import com.openai.code.Entity.CodeReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<CodeReview, Long>{
}
