package com.kv1k1.repository;

import com.kv1k1.entity.Ad;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AdRepository implements PanacheRepository<Ad> {

    public List<Ad> findActiveAds() {
        return find("isActive = true and (expiresAt is null or expiresAt > ?1)",
                LocalDateTime.now()).list();
    }

    public Optional<Ad> findRandomActiveAd() {
        List<Ad> activeAds = findActiveAds();
        if (activeAds.isEmpty()) {
            return Optional.empty();
        }
        int randomIndex = (int) (Math.random() * activeAds.size());
        return Optional.of(activeAds.get(randomIndex));
    }

    public void incrementDisplayCount(Long adId) {
        update("displayCount = displayCount + 1 where id = ?1", adId);
    }

    public void incrementClickCount(Long adId) {
        update("clickCount = clickCount + 1 where id = ?1", adId);
    }
}