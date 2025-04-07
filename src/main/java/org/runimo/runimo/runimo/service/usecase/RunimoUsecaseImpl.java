package org.runimo.runimo.runimo.service.usecase;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.runimo.controller.dto.response.GetMyRunimoListResponse;
import org.runimo.runimo.runimo.controller.dto.response.SetMainRunimoResponse;
import org.runimo.runimo.runimo.domain.RunimoDefinition;
import org.runimo.runimo.runimo.exception.RunimoException;
import org.runimo.runimo.runimo.exception.RunimoHttpResponseCode;
import org.runimo.runimo.runimo.repository.RunimoDefinitionRepository;
import org.runimo.runimo.runimo.repository.UserRunimoRepository;
import org.runimo.runimo.runimo.service.model.RunimoSimpleModel;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.service.UserFinder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RunimoUsecaseImpl implements RunimoUsecase{
    private final RunimoDefinitionRepository runimoDefinitionRepository;
    private final UserRunimoRepository userRunimoRepository;
    private final UserFinder userFinder;

    public GetMyRunimoListResponse getMyRunimoList(Long userId) {
        List<RunimoSimpleModel> runimos = userRunimoRepository.findAllByUserId(userId);
        return new GetMyRunimoListResponse(RunimoSimpleModel.toDtoList(runimos));
    }

    @Transactional
    public SetMainRunimoResponse setMainRunimo(Long userId, Long runimoId) {
        RunimoDefinition runimoDefinition = runimoDefinitionRepository.findById(runimoId)
                .orElseThrow(() -> RunimoException.of(RunimoHttpResponseCode.RUNIMO_NOT_FOUND));
        validateOwner(userId, runimoDefinition.getId());

        User user = userFinder.findUserById(userId)
                .orElseThrow(NoSuchElementException::new);

        user.updateMainRunimo(runimoId);

        return new SetMainRunimoResponse(runimoId);
    }

    private void validateOwner(Long userId, Long runimoId) {
        if(!userRunimoRepository.existsByUserIdAndRunimoDefinitionId(userId, runimoId)){
            throw RunimoException.of(RunimoHttpResponseCode.USER_DO_NOT_OWN_RUNIMO);
        }
    }
}
