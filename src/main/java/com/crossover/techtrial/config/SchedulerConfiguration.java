package com.crossover.techtrial.config;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.ScheduledLockConfiguration;
import net.javacrumbs.shedlock.spring.ScheduledLockConfigurationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.time.Duration;

/**
 * Configures the scheduler.
 *
 * @author Marcos Barbero
 */
@Component
@EnableScheduling
public class SchedulerConfiguration {

    // TODO: make it configurable
    private static final int DURATION_IN_HOURS = 1;

    @Bean
    public ScheduledLockConfiguration taskScheduler(final LockProvider lockProvider) {
        return ScheduledLockConfigurationBuilder
                .withLockProvider(lockProvider)
                .withPoolSize(10)
                .withDefaultLockAtMostFor(Duration.ofHours(DURATION_IN_HOURS))
                .build();
    }

    @Bean
    public LockProvider lockProvider(final DataSource dataSource) {
        return new JdbcTemplateLockProvider(dataSource);
    }
}
