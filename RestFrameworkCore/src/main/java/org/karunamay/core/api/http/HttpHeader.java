package org.karunamay.core.api.http;

import java.util.Map;
import java.util.Optional;

public interface HttpHeader {
    void set(String name, String value);
    Optional<String> get(String name);
    Map<String, String> asMap();
}
