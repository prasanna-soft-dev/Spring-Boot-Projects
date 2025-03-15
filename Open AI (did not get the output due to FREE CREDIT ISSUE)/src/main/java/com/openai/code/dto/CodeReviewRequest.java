package com.openai.code.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter


@ToString

public class CodeReviewRequest {
    private String code;
    private String lang;
}
