package org.karunamay.core.api.http;

import org.karunamay.core.api.dto.HttpRequestDTO;

public interface HttpBody<T> {

    HttpBody<? extends HttpRequestDTO> getValues();
}
