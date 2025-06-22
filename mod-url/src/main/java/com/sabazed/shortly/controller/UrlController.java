package com.sabazed.shortly.controller;

import com.sabazed.shortly.service.UrlService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class UrlController {

  private static final String ANALYTICS_ENDPOINT = "/api/analytics/click";

  private final UrlService urlService;
  private final String baseUrl;
  private final String analyticsUrl;
  private final RestTemplate restTemplate;

  public UrlController(UrlService urlService,
                       @Value("${app.base-url:http://localhost:8080}") String baseUrl,
                       @Value("${app.analytics-api-url:http://mod-analytics:8081}") String analyticsUrl) {
    this.urlService = urlService;
    this.baseUrl = baseUrl;
    this.analyticsUrl = analyticsUrl;
    this.restTemplate = new RestTemplate();
  }

  @PostMapping("/api/shorten")
  public ResponseEntity<Map<String, String>> shortenUrl(@RequestBody Map<String, String> request) {
    String originalUrl = request.get("originalUrl");
    if (originalUrl == null || originalUrl.isEmpty()) {
      return ResponseEntity.badRequest().body(Map.of("error", "URL is required"));
    }

    try {
      String shortCode = urlService.shortenUrl(originalUrl);
      String shortUrl = baseUrl + "/" + shortCode;
      return ResponseEntity.ok(Map.of(
          "shortCode", shortCode,
          "shortUrl", shortUrl,
          "originalUrl", originalUrl
      ));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("error", "Failed to shorten URL"));
    }
  }
  @GetMapping("/api/shorten/{shortCode}")
  public ResponseEntity<Map<String, String>> getUrlInfo(@PathVariable String shortCode) {
    String originalUrl = urlService.getOriginalUrl(shortCode);

    if (originalUrl != null) {
      return ResponseEntity.ok(Map.of(
          "shortCode", shortCode,
          "originalUrl", originalUrl
      ));
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/{shortCode}")
  public RedirectView redirectToOriginalUrl(@PathVariable String shortCode) {
    String originalUrl = urlService.getOriginalUrl(shortCode);

    if (originalUrl != null) {
      restTemplate.postForLocation(analyticsUrl + ANALYTICS_ENDPOINT, Map.of("shortCode", shortCode));
      return new RedirectView(originalUrl);
    } else {
      return new RedirectView("/404");
    }
  }

}