package org.runimo.runimo.common.response;

import lombok.Getter;
import org.runimo.runimo.exceptions.code.CustomResponseCode;

@Getter
public class SuccessResponse<T> extends Response {

    private final T payload;

    private SuccessResponse(final CustomResponseCode code, final T payload) {
        super(true, code);
        this.payload = payload;
    }

    public static SuccessResponse<Void> messageOnly(final CustomResponseCode code) {
        return new SuccessResponse<>(code, null);
    }

    public static <T> SuccessResponse<T> of(final CustomResponseCode code, final T payload) {
        return new SuccessResponse<>(code, payload);
    }
}
