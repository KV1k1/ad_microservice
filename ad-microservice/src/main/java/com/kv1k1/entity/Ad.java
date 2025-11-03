package com.kv1k1.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ads")
public class Ad extends PanacheEntity {

    @Column(name = "image_url", nullable = false)
    public String imageUrl;

    @Column(name = "target_url", nullable = false)
    public String targetUrl;

    @Column(name = "is_active", nullable = false)
    public Boolean isActive;

    @Column(name = "created_at", nullable = false)
    public LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false)
    public String createdBy;

    @Column(name = "expires_at")
    public LocalDateTime expiresAt;

    @Column(name = "display_count", nullable = false)
    public Integer displayCount;

    @Column(name = "click_count", nullable = false)
    public Integer clickCount;
}