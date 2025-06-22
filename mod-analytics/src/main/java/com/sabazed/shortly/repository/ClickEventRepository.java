package com.sabazed.shortly.repository;

import com.sabazed.shortly.entity.ClickEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {

  Long countByShortCode(String shortCode);

  Long countByShortCodeAndClickedAtAfter(String shortCode, LocalDateTime after);

  @Query("SELECT c.shortCode, COUNT(c) FROM ClickEvent c GROUP BY c.shortCode ORDER BY COUNT(c) DESC")
  List<Object[]> findTopClickedUrls();

  List<ClickEvent> findByShortCodeOrderByClickedAtDesc(String shortCode);

}