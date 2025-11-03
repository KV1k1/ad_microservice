package com.kv1k1.repository;

import com.kv1k1.entity.Ad;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
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

    @Transactional
    public void incrementDisplayCount(Long adId) {
        Ad ad = findById(adId);
        if (ad != null) {
            ad.displayCount++;
            persist(ad);
        }
    }

    @Transactional
    public void incrementClickCount(Long adId) {
        Ad ad = findById(adId);
        if (ad != null) {
            ad.clickCount++;
            persist(ad);
        }
    }
}