package com.ctz.gulimail.auth.vo;

import lombok.Data;

@Data
public class SocialUser {
    private String access_token;
    private String token_type;
    private int expires_in;
    private String refresh_token;
    private String scope;
    private int created_at;
}
