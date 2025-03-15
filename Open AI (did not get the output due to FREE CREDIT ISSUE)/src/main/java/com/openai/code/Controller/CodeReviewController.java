package com.openai.code.Controller;

import com.openai.code.Entity.CodeReview;
import com.openai.code.Service.CodeReviewService;
import com.openai.code.dto.CodeReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/review") // Base URL
public class CodeReviewController {

    @Autowired
    private CodeReviewService codeReviewService;

    // POST: Analyze Code & Store in DB
    @PostMapping
    public ResponseEntity<?> analyze(@RequestBody CodeReviewRequest codeReviewRequest) {
        CodeReview response = codeReviewService.analyzeCode(codeReviewRequest.getCode(), codeReviewRequest.getLang());
        return ResponseEntity.ok(response);
    }

    // GET: Fetch Review by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> reviewById(@PathVariable Long id) {
        Optional<CodeReview> response = Optional.ofNullable(codeReviewService.getReviewById(id));

        if (response.isPresent()) {
            return ResponseEntity.ok(response.get());
        } else {
            // Return a JSON response for errors
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Review not found");
            errorResponse.put("id", id.toString());

            return ResponseEntity.status(404).body(errorResponse);
        }
    }


    // GET: Fetch All Reviews
    @GetMapping
    public ResponseEntity<List<CodeReview>> getAllReviews() {
        List<CodeReview> response = codeReviewService.getAllReviews();
        return ResponseEntity.ok(response);
    }
}
