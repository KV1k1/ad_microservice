package com.kv1k1.service;

import com.kv1k1.entity.Ad;
import com.kv1k1.repository.AdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AdService {

    @Inject
    AdRepository adRepository;

    public List<Ad> getAllAds() {
        return adRepository.listAll();
    }

    public Optional<Ad> getRandomActiveAd() {
        return adRepository.findRandomActiveAd();
    }

    public Ad getAdById(Long id) {
        return adRepository.findById(id);
    }

    @Transactional
    public Ad createAd(Ad ad) {
        adRepository.persist(ad);
        return ad;
    }

    @Transactional
    public void recordAdDisplay(Long adId) {
        adRepository.incrementDisplayCount(adId);
    }

    @Transactional
    public void recordAdClick(Long adId) {
        adRepository.incrementClickCount(adId);
    }

    @Transactional
    public boolean deleteAd(Long id) {
        return adRepository.deleteById(id);
    }
}