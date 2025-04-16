package org.runimo.runimo.runimo.service.usecase;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.hatch.exception.HatchException;
import org.runimo.runimo.hatch.exception.HatchHttpResponseCode;
import org.runimo.runimo.runimo.service.dtos.response.GetMyRunimoListResponse;
import org.runimo.runimo.runimo.service.dtos.response.GetRunimoTypeListResponse;
import org.runimo.runimo.runimo.service.dtos.response.SetMainRunimoResponse;
import org.runimo.runimo.runimo.domain.Runimo;
import org.runimo.runimo.runimo.exception.RunimoException;
import org.runimo.runimo.runimo.exception.RunimoHttpResponseCode;
import org.runimo.runimo.runimo.repository.RunimoDefinitionRepository;
import org.runimo.runimo.runimo.repository.RunimoRepository;
import org.runimo.runimo.runimo.service.dtos.RunimoSimpleModel;
import org.runimo.runimo.runimo.service.dtos.RunimoTypeSimpleModel;
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
    private final RunimoDefinitionRepository runimoDefinitionRepository;

    public GetMyRunimoListResponse getMyRunimoList(Long userId) {
        List<RunimoSimpleModel> models = runimoRepository.findAllByUserId(userId);

        User user = userFinder.findUserById(userId).orElseThrow(() -> HatchException.of(
            HatchHttpResponseCode.HATCH_USER_NOT_FOUND));

        return new GetMyRunimoListResponse(
            RunimoSimpleModel.toDtoList(models, user.getMainRunimoId()));
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
        List<RunimoTypeSimpleModel> models = runimoDefinitionRepository.findAllToSimpleModel();
        return new GetRunimoTypeListResponse(RunimoTypeSimpleModel.toDtoList(models));
    }

}
