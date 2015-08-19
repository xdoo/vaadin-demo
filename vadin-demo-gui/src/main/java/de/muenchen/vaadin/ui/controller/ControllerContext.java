package de.muenchen.vaadin.ui.controller;

import de.muenchen.vaadin.demo.api.domain.BaseEntity;
import de.muenchen.vaadin.ui.app.views.events.AppEvent;

/**
 * Created by maximilian.zollbrecht on 19.08.15.
 */
public interface ControllerContext<E extends BaseEntity> {
    String resolveRelative(String relativePath);
    String resolve(String path);
    void postToEventBus(AppEvent<?> appEvent);
}
