package com.ctz.common.to;

import lombok.Data;

@Data
public class UserInfoTo {
    private Long userId;
    private String userKey;

    private Boolean hasUserKey = false;
}
