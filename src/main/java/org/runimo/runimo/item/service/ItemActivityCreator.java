package org.runimo.runimo.item.service;

import org.runimo.runimo.item.service.dto.CreateActivityCommand;

public interface ItemActivityCreator {

    void createItemActivity(CreateActivityCommand command);
}
