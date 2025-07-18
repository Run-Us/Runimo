package org.runimo.runimo.runimo.service.usecase;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.hatch.exception.HatchException;
import org.runimo.runimo.hatch.exception.HatchHttpResponseCode;
import org.runimo.runimo.item.domain.EggType;
import org.runimo.runimo.item.repository.EggTypeRepository;
import org.runimo.runimo.runimo.domain.Runimo;
import org.runimo.runimo.runimo.exception.RunimoException;
import org.runimo.runimo.runimo.exception.RunimoHttpResponseCode;
import org.runimo.runimo.runimo.repository.RunimoDefinitionRepository;
import org.runimo.runimo.runimo.repository.RunimoRepository;
import org.runimo.runimo.runimo.service.dto.RunimoSimpleModel;
import org.runimo.runimo.runimo.service.dto.RunimoTypeSimpleModel;
import org.runimo.runimo.runimo.service.dto.response.GetMyRunimoListResponse;
import org.runimo.runimo.runimo.service.dto.response.GetRunimoTypeListResponse;
import org.runimo.runimo.runimo.service.dto.response.RunimoTypeGroup;
import org.runimo.runimo.runimo.service.dto.response.RunimoTypeInfo;
import org.runimo.runimo.runimo.service.dto.response.SetMainRunimoResponse;
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
    private final EggTypeRepository eggTypeRepository;

    public GetMyRunimoListResponse getMyRunimoList(Long userId) {
        List<RunimoSimpleModel> models = runimoRepository.findAllByUserId(userId);

        User user = userFinder.findUserById(userId).orElseThrow(() -> HatchException.of(
            HatchHttpResponseCode.HATCH_USER_NOT_FOUND));

        return new GetMyRunimoListResponse(
            user.getTotalDistanceInMeters(),
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
        List<EggType> eggTypes = eggTypeRepository.findAllByOrderByIdAsc();

        Map<Long, List<RunimoTypeSimpleModel>> runimoTypeInfos =
            runimoDefinitionRepository.findRunimoSimpleTypeModelByType(eggTypes).stream()
                .collect(Collectors.groupingBy(RunimoTypeSimpleModel::getEggTypeId));

        List<RunimoTypeGroup> runimoTypeGroups = eggTypes.stream()
            .map(type -> new RunimoTypeGroup(
                type.getName(),
                type.getRequiredDistanceInMeters(),
                RunimoTypeInfo.from(
                    runimoTypeInfos.getOrDefault(type.getId(), Collections.emptyList()))
            ))
            .toList();

        return new GetRunimoTypeListResponse(runimoTypeGroups);
    }

}
