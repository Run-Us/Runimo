package org.runimo.runimo.auth.verifier;

import com.auth0.jwt.interfaces.DecodedJWT;

public interface OidcTokenVerifier {
  DecodedJWT verifyToken(DecodedJWT token);

}
