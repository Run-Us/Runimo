package org.runimo.runimo.user.service.usecases.eggs;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.user.service.IncubatingEggFinder;
import org.runimo.runimo.user.service.dto.IncubatingEggView;
import org.runimo.runimo.user.service.dto.response.QueryIncubatingEggResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IncubatingEggQueryUsecaseImpl implements IncubatingEggQueryUsecase {

    private final IncubatingEggFinder incubatingEggFinder;

    @Override
    public QueryIncubatingEggResponse execute(Long userId) {

        List<IncubatingEggView> incubatingEggs = incubatingEggFinder.findIncubatingEggsViewByUserId(
            userId);
        return new QueryIncubatingEggResponse(incubatingEggs);
    }
}
