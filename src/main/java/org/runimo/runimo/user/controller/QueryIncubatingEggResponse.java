package org.runimo.runimo.user.controller;

import org.runimo.runimo.user.service.dtos.IncubatingEggView;

import java.util.List;

public record QueryIncubatingEggResponse(
    List<IncubatingEggView> incubatingEggs
) {
}
