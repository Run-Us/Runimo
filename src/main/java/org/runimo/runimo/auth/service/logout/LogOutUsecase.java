package org.runimo.runimo.auth.service.logout;

public interface LogOutUsecase {

    void execute(String refreshToken);
}
