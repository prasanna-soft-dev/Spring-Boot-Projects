package com.openai.code.Service.Implementation;

import com.openai.code.Entity.CodeReview;
import com.openai.code.Repository.ReviewRepository;
import com.openai.code.Service.CodeReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CodeReviewServiceImpl implements CodeReviewService {
    @Value("${openai.api.model}")
    private String openAiModel;

    private final RestTemplate restTemplate;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    public CodeReviewServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public CodeReview analyzeCode(String code, String lang) {
        String feedback = getAiFeedback(code, lang);

        CodeReview codeReview = new CodeReview();
        codeReview.setCode(code);
        codeReview.setLang(lang);
        codeReview.setAiFeedback(feedback);

        return reviewRepository.save(codeReview);
    }

    private String getAiFeedback(String code, String lang) {
        String openAiUrl = "https://api.openai.com/v1/chat/completions";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", openAiModel);  // Update based on your model
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", "You are a code reviewer. Provide detailed feedback."),
                Map.of("role", "user", "content", "Review this " + lang + " code:\n\n" + code)
        ));
        requestBody.put("max_tokens", 100);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(openAiUrl, HttpMethod.POST, requestEntity, Map.class);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");

            if (!choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                return message.get("content").toString();
            }
        } catch (Exception e) {
            System.out.println("Error calling OpenAI: " + e.getMessage());
        }

        return "Error: Unable to get AI feedback.";
    }

    @Override
    public CodeReview getReviewById(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
    }

    @Override
    public List<CodeReview> getAllReviews() {
        return reviewRepository.findAll();
    }
}
