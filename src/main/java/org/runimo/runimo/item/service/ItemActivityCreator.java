package org.runimo.runimo.item.service;

import org.runimo.runimo.item.service.dtos.CreateActivityCommand;

public interface ItemActivityCreator {

    void createItemActivity(CreateActivityCommand command);
}
