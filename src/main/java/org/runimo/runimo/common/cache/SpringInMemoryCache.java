package org.runimo.runimo.common.cache;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.TaskScheduler;

public class SpringInMemoryCache<K, V> implements InMemoryCache<K, V>, InitializingBean,
    DisposableBean {

    private final ConcurrentHashMap<K, CacheEntry<V>> cache = new ConcurrentHashMap<>();
    private final TaskScheduler taskScheduler;
    private final Duration cleanupInterval;
    private ScheduledFuture<?> cleanupTask;

    public SpringInMemoryCache(TaskScheduler taskScheduler, Duration cleanupInterval) {
        this.taskScheduler = taskScheduler;
        this.cleanupInterval = cleanupInterval;
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, CacheEntry.permanent(value));
    }

    @Override
    public void put(K key, V value, Duration ttl) {
        cache.put(key, CacheEntry.withTtl(value, ttl));
    }

    @Override
    public boolean putIfAbsent(K key, V value) {
        return cache.putIfAbsent(key, CacheEntry.permanent(value)) == null;
    }

    @Override
    public boolean putIfAbsent(K key, V value, Duration ttl) {
        CacheEntry<V> newEntry = CacheEntry.withTtl(value, ttl);
        CacheEntry<V> existingEntry = cache.putIfAbsent(key, newEntry);

        if (existingEntry != null) {
            if (existingEntry.isExpired()) {
                cache.put(key, newEntry);
                return true;
            }
            return false;
        }
        return true;
    }

    @Override
    public Optional<V> get(K key) {
        CacheEntry<V> entry = cache.get(key);
        if (entry == null) {
            return Optional.empty();
        }

        if (entry.isExpired()) {
            cache.remove(key);
            return Optional.empty();
        }

        return Optional.of(entry.value());
    }

    public Optional<CacheEntry<V>> getEntry(K key) {
        CacheEntry<V> entry = cache.get(key);
        if (entry == null || entry.isExpired()) {
            if (entry != null && entry.isExpired()) {
                cache.remove(key);
            }
            return Optional.empty();
        }
        return Optional.of(entry);
    }

    @Override
    public boolean remove(K key) {
        return cache.remove(key) != null;
    }

    @Override
    public boolean remove(K key, V value) {
        CacheEntry<V> entry = cache.get(key);
        if (entry == null || !entry.value().equals(value)) {
            return false;
        }
        return cache.remove(key) != null;
    }

    public void cleanup() {
        cache.entrySet().removeIf(entry ->
            entry.getValue().expiresAt() != null &&
                entry.getValue().isExpired());
    }

    @Override
    public void afterPropertiesSet() {
        this.cleanupTask = taskScheduler.scheduleAtFixedRate(
            this::cleanup,
            cleanupInterval
        );
    }

    @Override
    public void destroy() {
        if (cleanupTask != null) {
            cleanupTask.cancel(false);
        }
    }
}

