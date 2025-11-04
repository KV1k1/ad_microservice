package com.kv1k1.dto;

import java.time.LocalDateTime;

public class AdDTO {
    public Long id;
    public String imageUrl;
    public String targetUrl;
    public Boolean isActive;
    public LocalDateTime createdAt;
    public String createdBy;
    public LocalDateTime expiresAt;
    public Integer displayCount;
    public Integer clickCount;

    public AdDTO() {}
}