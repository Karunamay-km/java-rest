package org.karunamay.core.api.router;

import org.karunamay.core.api.controller.RestControllerConfig;

public interface RouteComponent<T extends RestControllerConfig> {
    String getPath();

    Class<T> getController();

    String getName();

//    AbstractPathParams getPathParams();
}
