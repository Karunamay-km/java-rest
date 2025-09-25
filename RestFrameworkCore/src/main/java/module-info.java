module org.karunamay.core {
    requires org.slf4j;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.hibernate.orm.community.dialects;
    requires java.xml.crypto;
    requires jjwt.api;
    requires com.fasterxml.jackson.annotation;
    requires static lombok;
    requires jdk.jdi;

    exports org.karunamay.core.api.http;
    exports org.karunamay.core.api.controller;
    exports org.karunamay.core.api.router;
    exports org.karunamay.core.api.annotation;
    exports org.karunamay.core.api.config;
    exports org.karunamay.core.api.middleware;
    exports org.karunamay.core.api.dto;
    exports org.karunamay.core.api;
    exports org.karunamay.core.authentication.model;
    exports org.karunamay.core.authentication;

    uses org.karunamay.core.api.router.RouterConfig;

    opens org.karunamay.core.authentication.model to org.hibernate.orm.core;
    exports org.karunamay.core.api.router.Annotation;
    opens org.karunamay.core.authentication to org.hibernate.orm.core;
    exports org.karunamay.core.api.service;
    opens org.karunamay.core.api.service to org.hibernate.orm.core;
}