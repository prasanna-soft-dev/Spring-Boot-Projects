package com.jwt.token.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class AuthResponse {
    private String accessToken;
    private String refreshToken;

}
