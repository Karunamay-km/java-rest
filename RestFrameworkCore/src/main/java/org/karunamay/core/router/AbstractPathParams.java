package org.karunamay.core.router;

import java.util.HashMap;
import java.util.Map;

public abstract sealed class AbstractPathParams permits PathParams {

    private Map<String, String> pathParams = new HashMap<>();

    public AbstractPathParams() {
    }

    public void setPathParams(Map<String, String> pathParams) {
        this.pathParams = pathParams;
    }

    public Map<String, String> getPathParams() {
        return pathParams;
    }

}
