package com.crossover.techtrial.repository;

import com.crossover.techtrial.model.Panel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * PanelRepository allows all operations to Panel Entity.
 * @author Crossover
 */
public interface PanelRepository extends JpaRepository<Panel, Long> {

  /**
   * Finds a {@link Panel} by the given serial.
   *
   * @param serial The serial
   * @return {@link Panel}
   */
  Panel findBySerial(String serial);
}
