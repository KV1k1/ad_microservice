package com.kv1k1.dto;

import com.kv1k1.entity.Ad;

import java.time.LocalDateTime;

public class AdMapper {
    public static AdDTO toDto(Ad ad) {
        if (ad == null) return null;
        AdDTO dto = new AdDTO();
        dto.id = ad.id;
        dto.imageUrl = ad.imageUrl;
        dto.targetUrl = ad.targetUrl;
        dto.isActive = ad.isActive;
        dto.createdAt = ad.createdAt;
        dto.createdBy = ad.createdBy;
        dto.expiresAt = ad.expiresAt;
        dto.displayCount = ad.displayCount;
        dto.clickCount = ad.clickCount;
        return dto;
    }

    public static Ad toEntity(AdDTO dto) {
        if (dto == null) return null;
        Ad ad = new Ad();
        ad.imageUrl = dto.imageUrl;
        ad.targetUrl = dto.targetUrl;
        ad.isActive = dto.isActive != null ? dto.isActive : true;
        ad.createdAt = LocalDateTime.now();
        ad.createdBy = dto.createdBy;
        ad.expiresAt = dto.expiresAt;
        ad.displayCount = dto.displayCount != null ? dto.displayCount : 0;
        ad.clickCount = dto.clickCount != null ? dto.clickCount : 0;
        return ad;
    }
}