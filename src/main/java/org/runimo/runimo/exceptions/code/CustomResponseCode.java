package org.runimo.runimo.exceptions.code;

import org.springframework.http.HttpStatus;

public interface CustomResponseCode {

    String getCode();

    String getClientMessage();

    String getLogMessage();

    HttpStatus getHttpStatusCode();
}
