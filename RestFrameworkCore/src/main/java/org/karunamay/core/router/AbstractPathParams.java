package org.karunamay.core.router;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractPathParams {

    private List<Map<String, String>> parameters = new ArrayList<>();
    private Map<String, String> pathParams = new HashMap<>();

    public AbstractPathParams(String name, int index, String value) {

    }

    public void setPathParams(Map<String, String> pathParams) {
        this.pathParams = pathParams;
    }

    public Map<String, String> getPathParams() {
        return pathParams;
    }

}
