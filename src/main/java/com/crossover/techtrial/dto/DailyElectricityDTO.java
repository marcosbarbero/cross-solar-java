package com.crossover.techtrial.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DailyElectricityDTO class will hold sum, average,minimum and maximum electricity for a given day.
 *
 * @author Crossover
 * @author Marcos Barbero
 */
public class DailyElectricityDTO implements Serializable {
  
  private static final long serialVersionUID = 3605549122072628877L;

  @JsonIgnore
  private Long panelId;

  private LocalDate date;
  
  private Long sum;
  
  private Long average;
  
  private Long min;
  
  private Long max;

  public DailyElectricityDTO(LocalDate date, Long sum, Long average, Long min, Long max) {
    this.date = date;
    this.sum = sum;
    this.average = average;
    this.min = min;
    this.max = max;
  }

  public DailyElectricityDTO(Long panelId, LocalDate date, Long sum, Long average, Long min, Long max) {
    this(date, sum, average, min, max);
    this.panelId = panelId;
  }

  public DailyElectricityDTO() {
  }

  public Long getPanelId() {
    return panelId;
  }

  public void setPanelId(Long panelId) {
    this.panelId = panelId;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public Long getSum() {
    return sum;
  }

  public void setSum(Long sum) {
    this.sum = sum;
  }

  public Long getAverage() {
    return average;
  }

  public void setAverage(Long average) {
    this.average = average;
  }

  public Long getMin() {
    return min;
  }

  public void setMin(Long min) {
    this.min = min;
  }

  public Long getMax() {
    return max;
  }

  public void setMax(Long max) {
    this.max = max;
  }

  @Override
  public String toString() {
    return "DailyElectricityDTO [date=" + date + ", sum=" + sum + ", average="
        + average + ", min=" + min + ", max=" + max + "]";
  }

}
