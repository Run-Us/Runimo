package org.runimo.runimo.auth.service.apple;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.runimo.runimo.auth.exceptions.UserJwtException;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.auth.service.kakao.AppleUserInfo;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.ECKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppleTokenVerifier {
  private final RestTemplate restTemplate = new RestTemplate();
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final Map<String, RSAPublicKey> publicKeys = new HashMap<>();
  private final JwtTokenFactory jwtTokenFactory;

  @Value("${apple.client-id}")
  private String clientId;

  @Value("${apple.redirect-uri}")
  private String redirectUri;

  @Value("${apple.team-id}")
  private String appleTeamId;

  @Value("${apple.key-id}")
  private String appleKeyId;

  @Value("${apple.client-secret}")
  private String applePrivateKey;

  @Scheduled(fixedRate = 3600000)
  public void refreshPublicKeys() {
    String jwksUrl = "https://appleid.apple.com/auth/keys";
    String jwksResponse = restTemplate.getForObject(jwksUrl, String.class);
    try {
      JsonNode jwks = objectMapper.readTree(jwksResponse);
      for (JsonNode key : jwks.get("keys")) {
        String kid = key.get("kid").asText();
        String n = key.get("n").asText();
        String e = key.get("e").asText();

        RSAPublicKey publicKey = createPublicKey(n, e);
        publicKeys.put(kid, publicKey);
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to refresh Apple public keys", e);
    }
  }

  private RSAPublicKey createPublicKey(String modulusBase64, String exponentBase64)
      throws Exception {
    byte[] modulusBytes = Base64.getUrlDecoder().decode(modulusBase64);
    byte[] exponentBytes = Base64.getUrlDecoder().decode(exponentBase64);

    BigInteger modulus = new BigInteger(1, modulusBytes);
    BigInteger exponent = new BigInteger(1, exponentBytes);

    RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
    KeyFactory factory = KeyFactory.getInstance("RSA");
    return (RSAPublicKey) factory.generatePublic(spec);
  }

  public String getAccessTokenFromAuthCode(String authCode, String codeVerifier) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("client_id", clientId);
    map.add("client_secret", generateAppleClientSecret());
    map.add("code", authCode);
    map.add("grant_type", "authorization_code");
    map.add("redirect_uri", redirectUri);
    map.add("code_verifier", codeVerifier);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

    ResponseEntity<String> response = restTemplate.postForEntity(
        "https://appleid.apple.com/auth/token", request, String.class);

    try {
      JsonNode node = objectMapper.readTree(response.getBody());
      return node.get("id_token").asText();
    } catch (Exception e) {
      log.error("Failed to verify Apple access token", e);
      throw UserJwtException.of(UserHttpResponseCode.TOKEN_INVALID);
    }
  }

  public AppleUserInfo verifyToken(DecodedJWT token) {
    try {
      RSAPublicKey publicKey = publicKeys.get(token.getKeyId());
      if (publicKey == null) {
        throw new JWTVerificationException("Unable to find appropriate Apple key");
      }

      Algorithm algorithm = Algorithm.RSA256(publicKey, null);

      DecodedJWT decodedJWT = JWT.require(algorithm)
          .withIssuer("https://appleid.apple.com")
          .withAudience(clientId)
          .build()
          .verify(token);

      String subject = decodedJWT.getSubject();
      String name = decodedJWT.getClaim("name").asString();
      return new AppleUserInfo(subject, name);
    } catch (JWTVerificationException exception) {
      log.error("Failed to verify Apple access token", exception);
      throw new UserJwtException(UserHttpResponseCode.JWT_TOKEN_BROKEN, exception.getMessage());
    }
  }

  /**
   * Apple의 권장 방식대로 JWT 형태의 클라이언트 시크릿을 동적으로 생성
   * 해당 시크릿은 6개월 동안 유효함
   */
  private String generateAppleClientSecret() {
    try {
      // 현재 시간 및 6개월 후 만료 시간 설정
      long nowMillis = System.currentTimeMillis();
      long expMillis = nowMillis + 180L * 24 * 60 * 60 * 1000; // 180일

      // 개인 키 포맷팅 및 디코딩
      String privateKeyContent = applePrivateKey.replace("\\n", "").trim();
      String formattedKey = "-----BEGIN PRIVATE KEY-----\n" + privateKeyContent + "\n-----END PRIVATE KEY-----";

      byte[] decodedKey = Base64.getDecoder().decode(
          formattedKey
              .replace("-----BEGIN PRIVATE KEY-----", "")
              .replace("-----END PRIVATE KEY-----", "")
              .replaceAll("\\s", "") // 모든 공백 문자 제거
      );

      // 개인 키 생성
      KeyFactory keyFactory = KeyFactory.getInstance("EC");
      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
      PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

      // JWT 클라이언트 시크릿 생성
      return JWT.create()
          .withHeader(Map.of("kid", appleKeyId))
          .withIssuer(appleTeamId)
          .withIssuedAt(new Date(nowMillis))
          .withExpiresAt(new Date(expMillis))
          .withAudience("https://appleid.apple.com")
          .withSubject(clientId)
          .sign(Algorithm.ECDSA256((ECKey) privateKey));

    } catch (Exception e) {
      log.error("Failed to verify Apple access token", e);
      throw new RuntimeException("Failed to generate Apple client secret", e);
    }
  }

  /**
   * 애플 토큰 검증을 위한 공개키를 의존성 주입 이후에 조회하여 캐싱하는 메소드
   */
  @PostConstruct
  public void initAppleTokenVerifier() {
    refreshPublicKeys();
  }
}