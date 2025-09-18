package org.karunamay.core.api.router;

import org.karunamay.core.api.http.HttpRequest;

public interface RouterConfig {
    void configure(RouteRegistry registry);
//    void configure(RouteRegistry registry, HttpRequest request);
}
