package org.runimo.runimo.user.service.usecases.eggs;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.user.service.dtos.QueryIncubatingEggResponse;
import org.runimo.runimo.user.service.dtos.IncubatingEggView;
import org.runimo.runimo.user.service.IncubatingEggFinder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncubatingEggQueryUsecaseImpl implements IncubatingEggQueryUsecase {
  private final IncubatingEggFinder incubatingEggFinder;

  @Override
  public QueryIncubatingEggResponse execute(Long userId) {

    List<IncubatingEggView> incubatingEggs = incubatingEggFinder.findIncubatingEggsViewByUserId(userId);
    return new QueryIncubatingEggResponse(incubatingEggs);
  }
}
