package com.kv1k1.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ads")
public class Ad extends PanacheEntity {

    @Column(nullable = false)
    public String imageUrl;

    @Column(nullable = false)
    public String targetUrl;

    @Column(nullable = false)
    public Boolean isActive;

    @Column(nullable = false)
    public LocalDateTime createdAt;

    @Column(nullable = false)
    public String createdBy;

    public LocalDateTime expiresAt;

    @Column(nullable = false)
    public Integer displayCount;

    @Column(nullable = false)
    public Integer clickCount;
}