package org.runimo.runimo.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.runimo.runimo.exceptions.code.CustomResponseCode;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Response {

    @Schema(description = "성공 여부")
    private boolean success;
    @Schema(description = "응답 메세지")
    private String message;
    @Schema(description = "응답 코드")
    private String code;

    protected Response(final boolean success, final String message, final String code) {
        this.success = success;
        this.message = message;
        this.code = code;
    }

    protected Response(final boolean success, final CustomResponseCode code) {
        this.success = success;
        this.message = code.getClientMessage();
        this.code = code.getCode();
    }
}

