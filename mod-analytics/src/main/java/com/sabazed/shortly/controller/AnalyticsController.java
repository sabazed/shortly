package com.sabazed.shortly.controller;

import com.sabazed.shortly.service.AnalyticsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class AnalyticsController {

  private final AnalyticsService analyticsService;

  @PostMapping("/click")
  public ResponseEntity<String> recordClick(@RequestBody Map<String, String> request,
                                            HttpServletRequest httpRequest) {
    String shortCode = request.get("shortCode");
    String ipAddress = getClientIpAddress(httpRequest);
    String userAgent = httpRequest.getHeader("User-Agent");
    String referer = httpRequest.getHeader("Referer");

    analyticsService.recordClick(shortCode, ipAddress, userAgent, referer);

    return ResponseEntity.ok("Click recorded");
  }

  @GetMapping("/{shortCode}")
  public ResponseEntity<Map<String, Object>> getAnalytics(@PathVariable String shortCode) {
    Map<String, Object> analytics = analyticsService.getAnalytics(shortCode);
    return ResponseEntity.ok(analytics);
  }

  @GetMapping("/top")
  public ResponseEntity<List<Object[]>> getTopUrls() {
    List<Object[]> topUrls = analyticsService.getTopUrls();
    return ResponseEntity.ok(topUrls);
  }

  private String getClientIpAddress(HttpServletRequest request) {
    String xForwardedForHeader = request.getHeader("X-Forwarded-For");
    if (xForwardedForHeader == null) {
      return request.getRemoteAddr();
    } else {
      return xForwardedForHeader.split(",")[0];
    }
  }
}