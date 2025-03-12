package org.runimo.runimo.auth.verifier;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.repository.OAuthTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoTokenVerifier implements OidcTokenVerifier {

  @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
  private String appKey;
  private final OAuthTokenRepository oAuthTokenRepository;
  private final RestTemplate restTemplate = new RestTemplate();
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final Map<String, RSAPublicKey> publicKeys = new HashMap<>();

  @Scheduled(fixedRate = 3600000)
  public void refreshPublicKeys() {
    String jwksUrl = "https://kauth.kakao.com/.well-known/jwks.json";
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
      throw new RuntimeException("Failed to refresh public keys", e);
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

  @Override
  public DecodedJWT verifyToken(DecodedJWT token) {
    try {
      String nonce = token.getClaim("nonce").asString();

      if (nonce == null) {
        throw new JWTVerificationException("Token nonce is missing");
      }

      RSAPublicKey publicKey = publicKeys.get(token.getKeyId());
      if (publicKey == null) {
        throw new JWTVerificationException("Unable to find appropriate key");
      }

      Algorithm algorithm = Algorithm.RSA256(publicKey, null);

      return JWT.require(algorithm)
          .withIssuer("https://kauth.kakao.com")
          .withAudience(appKey)
          .build()
          .verify(token);
    } catch (JWTVerificationException exception) {
      throw new IllegalArgumentException("ID token verification failed", exception);
    }
  }

  /*
  * 카카오 토큰 검증을 위한 공개키를 의존성 주입 이후에 조회하여 캐싱하는 메소드
  * */
  @PostConstruct
  public void initKakaoTokenVerifier() {
    refreshPublicKeys();
  }
}

