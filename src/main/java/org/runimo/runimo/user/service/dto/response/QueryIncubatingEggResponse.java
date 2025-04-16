package org.runimo.runimo.user.service.dto.response;

import java.util.List;
import org.runimo.runimo.user.service.dto.IncubatingEggView;

public record QueryIncubatingEggResponse(
    List<IncubatingEggView> incubatingEggs
) {

}
