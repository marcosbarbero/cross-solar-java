package com.crossover.techtrial.repository;

import com.crossover.techtrial.model.InitialProcess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository is for all operations for all {@link InitialProcess}.
 *
 * @author Marcos Barbero
 */
public interface InitialProcessRepository extends JpaRepository<InitialProcess, Long> {

    Optional<InitialProcess> findTopByOrderById();
}
