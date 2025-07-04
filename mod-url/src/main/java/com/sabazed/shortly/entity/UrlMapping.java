package com.sabazed.shortly.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "url_mappings")
@NoArgsConstructor
@Data
public class UrlMapping {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String shortCode;

  @Column(nullable = false, length = 2048)
  private String originalUrl;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime expiresAt;

  public UrlMapping(String shortCode, String originalUrl) {
    this.shortCode = shortCode;
    this.originalUrl = originalUrl;
    this.createdAt = LocalDateTime.now();
    this.expiresAt = LocalDateTime.now().plusDays(30);
  }

}