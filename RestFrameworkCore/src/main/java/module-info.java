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
    requires java.management;

    exports org.karunamay.core.api.http;
    exports org.karunamay.core.api.controller;
    exports org.karunamay.core.api.router;
    exports org.karunamay.core.api.annotation;
    exports org.karunamay.core.api.config;
    exports org.karunamay.core.api.middleware;
    exports org.karunamay.core.api.dto;
    exports org.karunamay.core.api.model;
    exports org.karunamay.core.api;
    exports org.karunamay.core.model;
    exports org.karunamay.core.api.router.Annotation;
    exports org.karunamay.core.api.service;
    exports org.karunamay.core.mapper;
    exports org.karunamay.core.repository;

    uses org.karunamay.core.api.router.RouterConfig;

    opens org.karunamay.core.model to org.hibernate.orm.core;
    opens org.karunamay.core.api.service to org.hibernate.orm.core;
    opens org.karunamay.core.repository to org.hibernate.orm.core;
    opens org.karunamay.core.mapper to org.hibernate.orm.core;

    provides org.karunamay.core.api.router.RouterConfig
            with org.karunamay.core.routes.Authentication;
}