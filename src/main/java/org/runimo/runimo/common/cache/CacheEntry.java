package org.runimo.runimo.common.cache;

import java.time.Duration;
import java.time.Instant;
import org.springframework.lang.Nullable;

public record CacheEntry<V>(
    V value,
    @Nullable
    Instant expiresAt
) {

    public static <V> CacheEntry<V> permanent(V value) {
        return new CacheEntry<>(value, null);
    }

    public static <V> CacheEntry<V> withTtl(V value, Duration ttl) {
        return new CacheEntry<>(value, Instant.now().plus(ttl));
    }

    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(Instant.now());
    }
}