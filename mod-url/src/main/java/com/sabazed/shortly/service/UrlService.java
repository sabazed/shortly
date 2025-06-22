package com.sabazed.shortly.service;

import com.sabazed.shortly.entity.UrlMapping;
import com.sabazed.shortly.repository.UrlMappingRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UrlService {

  private UrlMappingRepository urlMappingRepository;
  private RedisTemplate<String, String> redisTemplate;

  public String shortenUrl(String originalUrl) {
    // Check if URL already exists
    Optional<UrlMapping> existing = urlMappingRepository.findByOriginalUrl(originalUrl);
    if (existing.isPresent()) {
      return existing.get().getShortCode();
    }

    // Generate unique short code
    String shortCode;
    do {
      shortCode = RandomStringUtils.randomAlphanumeric(6);
    } while (urlMappingRepository.findByShortCode(shortCode).isPresent());

    // Save to database
    UrlMapping urlMapping = new UrlMapping(shortCode, originalUrl);
    urlMappingRepository.save(urlMapping);

    // Cache in Redis
    redisTemplate.opsForValue().set("url:" + shortCode, originalUrl, Duration.ofDays(30));

    return shortCode;
  }

  public String getOriginalUrl(String shortCode) {
    // Try Redis first
    String cachedUrl = redisTemplate.opsForValue().get("url:" + shortCode);
    if (cachedUrl != null) {
      return cachedUrl;
    }

    // Fallback to database
    Optional<UrlMapping> urlMapping = urlMappingRepository.findByShortCode(shortCode);
    if (urlMapping.isPresent()) {
      String originalUrl = urlMapping.get().getOriginalUrl();
      // Cache result
      redisTemplate.opsForValue().set("url:" + shortCode, originalUrl, Duration.ofDays(30));
      return originalUrl;
    }

    return null;
  }
}