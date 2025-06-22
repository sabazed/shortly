package com.sabazed.shortly.service;

import com.sabazed.shortly.entity.ClickEvent;
import com.sabazed.shortly.repository.ClickEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class AnalyticsService {

  private final ClickEventRepository clickEventRepository;

  public void recordClick(String shortCode, String ipAddress, String userAgent, String referer) {
    ClickEvent clickEvent = new ClickEvent(shortCode, ipAddress, userAgent, referer);
    clickEventRepository.save(clickEvent);
  }

  public Map<String, Object> getAnalytics(String shortCode) {
    Long totalClicks = clickEventRepository.countByShortCode(shortCode);
    Long todayClicks = clickEventRepository.countByShortCodeAndClickedAtAfter(
        shortCode, LocalDateTime.now().minusDays(1));

    List<ClickEvent> recentClicks = clickEventRepository
        .findByShortCodeOrderByClickedAtDesc(shortCode);

    Map<String, Object> analytics = new HashMap<>();
    analytics.put("shortCode", shortCode);
    analytics.put("totalClicks", totalClicks);
    analytics.put("todayClicks", todayClicks);
    analytics.put("recentClicks", recentClicks.stream().limit(10).toList());

    return analytics;
  }

  public List<Object[]> getTopUrls() {
    return clickEventRepository.findTopClickedUrls();
  }
}