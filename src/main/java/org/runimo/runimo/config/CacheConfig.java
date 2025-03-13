package org.runimo.runimo.config;

import com.sun.security.auth.UserPrincipal;
import org.runimo.runimo.auth.jwt.TokenStatus;
import org.runimo.runimo.common.cache.InMemoryCache;
import org.runimo.runimo.common.cache.SpringInMemoryCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Duration;

@Configuration
public class CacheConfig {

  @Value("${cache.cleanup.interval:300}")
  private int cleanupIntervalSeconds;

  @Value("${cache.cleanup.thread-pool-size:1}")
  private int cleanupThreadPoolSize;

  @Bean
  public TaskScheduler cacheCleanupScheduler() {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.setPoolSize(cleanupThreadPoolSize);
    scheduler.setThreadNamePrefix("cache-cleanup-");
    scheduler.setDaemon(true);
    scheduler.setWaitForTasksToCompleteOnShutdown(true);
    scheduler.setAwaitTerminationSeconds(10);
    return scheduler;
  }

  @Bean
  public InMemoryCache<String, TokenStatus> tokenStatusCache(TaskScheduler cacheCleanupScheduler) {
    return new SpringInMemoryCache<>(
        cacheCleanupScheduler,
        Duration.ofSeconds(cleanupIntervalSeconds)
    );
  }

  @Bean
  public InMemoryCache<String, UserPrincipal> userPrincipalCache(
      TaskScheduler cacheCleanupScheduler
  ) {
    return new SpringInMemoryCache<>(
        cacheCleanupScheduler,
        Duration.ofSeconds(cleanupIntervalSeconds)
    );
  }
}
