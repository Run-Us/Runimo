package org.runimo.runimo.auth.service.login.apple;

import org.runimo.runimo.auth.service.dto.TokenPair;

public class AppleAuthContext {

    private static final ThreadLocal<TokenPair> TOKENS = new ThreadLocal<>();

    public static TokenPair getCurrentTokens() {
        return TOKENS.get();
    }

    public static void setCurrentTokens(TokenPair tokens) {
        TOKENS.set(tokens);
    }

    public static void clear() {
        TOKENS.remove();
    }
}
