package org.runimo.runimo.user.service.dtos.response;

import java.util.List;
import org.runimo.runimo.user.service.dtos.IncubatingEggView;

public record QueryIncubatingEggResponse(
    List<IncubatingEggView> incubatingEggs
) {

}
