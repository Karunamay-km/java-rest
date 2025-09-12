module org.karunamay.core {
    requires org.slf4j;
    requires com.fasterxml.jackson.databind;

    exports org.karunamay.core.api.http;
    exports org.karunamay.core.api.controller;
    exports org.karunamay.core.api.router;
    exports org.karunamay.core.api.annotation;
    exports org.karunamay.core.internal;

    uses org.karunamay.core.api.router.RouterConfig;

}