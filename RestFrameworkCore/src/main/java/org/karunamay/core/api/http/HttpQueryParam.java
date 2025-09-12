package org.karunamay.core.api.http;

import java.util.Map;

public interface HttpQueryParam {
    void set(String key, String value);
    Map<String, String> getQueryParam();
}
