package org.runimo.runimo.runimo.service.usecase;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.runimo.controller.dto.response.GetMyRunimoListResponse;
import org.runimo.runimo.runimo.controller.dto.response.GetRunimoTypeListResponse;
import org.runimo.runimo.runimo.controller.dto.response.SetMainRunimoResponse;
import org.runimo.runimo.runimo.domain.Runimo;
import org.runimo.runimo.runimo.exception.RunimoException;
import org.runimo.runimo.runimo.exception.RunimoHttpResponseCode;
import org.runimo.runimo.runimo.repository.RunimoRepository;
import org.runimo.runimo.runimo.service.model.RunimoSimpleModel;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.service.UserFinder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RunimoUsecaseImpl implements RunimoUsecase {

    private final RunimoRepository runimoRepository;
    private final UserFinder userFinder;

    public GetMyRunimoListResponse getMyRunimoList(Long userId) {
        List<RunimoSimpleModel> runimos = runimoRepository.findAllByUserId(userId);
        return new GetMyRunimoListResponse(RunimoSimpleModel.toDtoList(runimos));
    }

    @Transactional
    public SetMainRunimoResponse setMainRunimo(Long userId, Long runimoId) {
        Runimo runimo = runimoRepository.findById(runimoId)
            .orElseThrow(() -> RunimoException.of(RunimoHttpResponseCode.RUNIMO_NOT_FOUND));
        runimo.validateOwner(userId);

        User user = userFinder.findUserById(userId)
            .orElseThrow(NoSuchElementException::new);

        user.updateMainRunimo(runimoId);

        return new SetMainRunimoResponse(runimoId);
    }

    @Override
    public GetRunimoTypeListResponse getRunimoTypeList() {
        return null;
    }

}
