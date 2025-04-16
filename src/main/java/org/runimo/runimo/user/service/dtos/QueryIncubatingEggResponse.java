package org.runimo.runimo.user.service.dtos;

import java.util.List;

public record QueryIncubatingEggResponse(
    List<IncubatingEggView> incubatingEggs
) {

}
