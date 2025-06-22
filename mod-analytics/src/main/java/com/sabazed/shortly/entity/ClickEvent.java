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
@Table(name = "click_events")
@NoArgsConstructor
@Data
public class ClickEvent {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String shortCode;

  @Column(nullable = false)
  private String ipAddress;

  @Column
  private String userAgent;

  @Column
  private String referer;

  @Column(nullable = false)
  private LocalDateTime clickedAt;

  public ClickEvent(String shortCode, String ipAddress, String userAgent, String referer) {
    this.shortCode = shortCode;
    this.ipAddress = ipAddress;
    this.userAgent = userAgent;
    this.referer = referer;
    this.clickedAt = LocalDateTime.now();
  }

}