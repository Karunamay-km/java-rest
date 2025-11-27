package org.karunamay.core.http;

import lombok.Getter;
import org.karunamay.core.api.dto.HttpRequestDTO;
import org.karunamay.core.api.http.HttpBody;

@Getter
public class HttpBodyImpl<T extends HttpRequestDTO> implements HttpBody<T> {

    private final HttpBody<? extends HttpRequestDTO> values;

    public HttpBodyImpl(HttpBody<? extends HttpRequestDTO> values) {
        this.values = values;
    }

    public HttpBody<? extends HttpRequestDTO> getValues() {
        return values;
    }
}
