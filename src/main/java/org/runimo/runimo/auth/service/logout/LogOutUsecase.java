package org.runimo.runimo.auth.service.logout;

public interface LogOutUsecase {

    boolean execute(String refreshToken);
}
