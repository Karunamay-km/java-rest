package org.karunamay.core.internal;

import org.karunamay.core.api.http.HttpQueryParam;

import java.util.HashMap;
import java.util.Map;

class HttpQueryParamImpl implements HttpQueryParam {

    private final Map<String, String> queryParam = new HashMap<>();

    public void set(String key, String value) {
        queryParam.put(key, value);
    }

    public Map<String, String> getQueryParam() {
        return queryParam;
    }
}
