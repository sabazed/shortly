package com.sabazed.shortly.repository;

import com.sabazed.shortly.entity.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

  Optional<UrlMapping> findByShortCode(String shortCode);
  Optional<UrlMapping> findByOriginalUrl(String originalUrl);

}
