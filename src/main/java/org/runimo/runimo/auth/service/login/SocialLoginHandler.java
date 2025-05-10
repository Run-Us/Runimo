package org.runimo.runimo.auth.service.login;

import org.runimo.runimo.auth.service.dto.AuthResult;

public interface SocialLoginHandler {

    AuthResult validateAndLogin(Object... params);

}
