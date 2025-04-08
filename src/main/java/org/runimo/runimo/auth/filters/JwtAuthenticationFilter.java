package org.runimo.runimo.auth.filters;

import static org.runimo.runimo.common.GlobalConsts.WHITE_LIST_ENDPOINTS;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.runimo.runimo.auth.jwt.JwtResolver;
import org.runimo.runimo.common.response.ErrorResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String AUTH_PREFIX = "Bearer ";
    private static final int TOKEN_PREFIX_LENGTH = 7;
    private final JwtResolver jwtResolver;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
        throws IOException {
        try {
            if (!hasValidAuthorizationHeader(request)) {
                setErrorResponse(UserErrorCode.JWT_NOT_FOUND, response);
                return;
            }
            String jwtToken = extractToken(request);
            if (!processToken(jwtToken, response)) {
                return;
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.warn("[ERROR] JWT broken : {}", e.getMessage());
            setErrorResponse(UserErrorCode.JWT_BROKEN, response);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private boolean hasValidAuthorizationHeader(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER);
        return authHeader != null && authHeader.startsWith(AUTH_PREFIX);
    }

    private String extractToken(HttpServletRequest request) {
        return request.getHeader(AUTH_HEADER).substring(TOKEN_PREFIX_LENGTH);
    }

    private boolean processToken(String jwtToken, HttpServletResponse response) throws IOException {
        try {
            String userId = jwtResolver.getUserIdFromJwtToken(jwtToken);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                userId,
                null,
                Collections.emptyList()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        } catch (TokenExpiredException e) {
            setErrorResponse(UserErrorCode.JWT_EXPIRED, response);
        } catch (Exception e) { // JWT 손상 시 오류
            log.warn("[ERROR]JWT broken : {}", e.getMessage());
            setErrorResponse(UserErrorCode.JWT_BROKEN, response);
        }
        return false;
    }

    private void setErrorResponse(UserErrorCode errorCode, HttpServletResponse response)
        throws IOException {
        log.warn(errorCode.name(), errorCode.getClientMessage());
        response.setStatus(errorCode.getHttpStatusCode().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    // JWT 필터는 로그인, 회원가입, 테스트용 API, 웹소켓 연결 요청에 대해서는 필터링을 하지 않는다.
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return WHITE_LIST_ENDPOINTS.stream()
            .anyMatch(endpoint -> request.getRequestURI().startsWith(endpoint));
    }
}