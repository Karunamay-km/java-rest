package org.karunamay.core.http;

import org.karunamay.core.api.http.HttpHeader;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpHeaderFactory implements HttpHeader {

    private final Map<String, String> headers = new HashMap<>();

    public HttpHeaderFactory() {
        setDefaultHeaders();
    }

    @Override
    public void set(String name, String value) {
        headers.put(name, value);
    }

    @Override
    public Optional<String> get(String name) {
        return Optional.ofNullable(headers.get(name));
    }

    @Override
    public Map<String, String> asMap() {
        return Collections.unmodifiableMap(headers);
    }

    public HttpHeader create() {
        return new HttpHeaderFactory();
    }

    public HttpHeader getHeaders() {
        return this;
    }

    private void setDefaultHeaders() {
        headers.put("Connection", "keep-alive");
        headers.put("Content-Type", "application/json");
        headers.put("Server", "ubuntu 24");
    }
}
