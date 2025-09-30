package org.karunamay.core.routes;

import org.karunamay.core.api.router.RouteFactory;
import org.karunamay.core.api.router.RouteRegistry;
import org.karunamay.core.api.router.RouterConfig;
import org.karunamay.core.controller.LoginController;

import java.util.List;

public class Authentication implements RouterConfig {
    @Override
    public void configure(RouteRegistry registry) {
        registry.add(() -> List.of(
                RouteFactory.create("/auth/login/", LoginController.class, "login", true)
        ));
    }
}

