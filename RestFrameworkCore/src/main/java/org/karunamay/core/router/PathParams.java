package org.karunamay.core.router;

import java.util.ArrayList;
import java.util.List;

final public class PathParams {

    private List<PathParameterImpl> parameterImplList = new ArrayList<>();

    public List<PathParameterImpl> getParameterList() {
        return parameterImplList;
    }
}
