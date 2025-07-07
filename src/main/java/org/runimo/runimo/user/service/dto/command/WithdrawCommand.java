package org.runimo.runimo.user.service.dto.command;

import org.runimo.runimo.user.domain.WithdrawalReason;

public record WithdrawCommand(
    Long userId,
    WithdrawalReason reason,
    String reasonDetail
) {

}
