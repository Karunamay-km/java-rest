module org.karunamay.core {
    requires org.slf4j;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.hibernate.orm.community.dialects;

    exports org.karunamay.core.api.http;
    exports org.karunamay.core.api.controller;
    exports org.karunamay.core.api.router;
    exports org.karunamay.core.api.annotation;
    exports org.karunamay.core.api.config;
    exports org.karunamay.core.api;
    exports org.karunamay.core.authentication.model;

    uses org.karunamay.core.api.router.RouterConfig;

    opens org.karunamay.core.authentication.model to org.hibernate.orm.core;
}