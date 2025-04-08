package org.runimo.runimo.user.service.dtos;

import org.runimo.runimo.runimo.service.model.MainRunimoStat;

public record MainViewResponse(
    MainRunimoStat mainRunimoStatNullable,
    UserMainViewInfo userInfo
) {

}
