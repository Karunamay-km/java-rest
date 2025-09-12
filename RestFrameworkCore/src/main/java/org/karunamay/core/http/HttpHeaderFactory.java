package org.karunamay.core.http;

import org.karunamay.core.api.http.HttpHeader;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpHeaderFactory implements HttpHeader {

    private static final Map<String, String> headers = new HashMap<>();

    HttpHeaderFactory() {
        headers.put("Connection", "keep-alive");
        headers.put("Content-Type", "application/json");
        headers.put("Server", "ubuntu 24");
    }

    public void set(String name, String value) {
        headers.put(name, value);
    }

    public Optional<String> get(String name) {
        return Optional.ofNullable(headers.get(name.toLowerCase()));
    }

    public Map<String, String> asMap() {
        return Collections.unmodifiableMap(headers);
    }

    public static HttpHeader create() {
        return new HttpHeaderFactory();
    }
}
