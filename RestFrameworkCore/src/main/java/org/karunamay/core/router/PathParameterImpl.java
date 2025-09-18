package org.karunamay.core.router;

import org.karunamay.core.api.router.PathParameter;

public class PathParameterImpl implements PathParameter {

    private final String name;
    private final int index;
    private final String value;

    public PathParameterImpl(String name, int index, String value) {
        this.name = name;
        this.index = index;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public String getValue() {
        return value;
    }
}
