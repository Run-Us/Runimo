package org.runimo.runimo.user.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.runimo.runimo.user.domain.WithdrawalReason;
import org.runimo.runimo.user.service.dto.command.WithdrawCommand;

@Schema(description = "회원 탈퇴 DTO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class WithdrawRequest {

    @Schema(description = "탈퇴 이유", example = "FOUND_BETTER_SERVICE")
    private String withdrawReason;
    @Schema(description = "기타 항목에 적는 문자열", example = "기타 항목에 적는 문자열")
    @Length(max = 255, message = "기타 항목에 적는 문자열은 255자 이하로 입력해주세요.")
    private String reasonDetail;

    public static WithdrawCommand toCommand(Long id, WithdrawRequest request) {
        return new WithdrawCommand(
            id,
            WithdrawalReason.valueOf(request.getWithdrawReason()),
            request.getReasonDetail()
        );
    }
}
