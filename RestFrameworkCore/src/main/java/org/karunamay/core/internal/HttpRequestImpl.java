package org.karunamay.core.internal;

import com.fasterxml.jackson.core.type.TypeReference;
import org.karunamay.core.api.dto.HttpRequestDTO;
import org.karunamay.core.api.http.HttpHeader;
import org.karunamay.core.api.http.HttpMethod;
import org.karunamay.core.api.http.HttpQueryParam;
import org.karunamay.core.api.http.HttpRequest;
import org.karunamay.core.utils.JsonParser;

class HttpRequestImpl implements HttpRequest {

    private final HttpMethod method;
    private final String path;
    private final HttpHeader headers;
    private final HttpQueryParam queryParams;
    private final String httpVersion;
    private final String body;

    HttpRequestImpl(
            HttpMethod method,
            String path,
            HttpHeader headers,
            HttpQueryParam queryParams,
            String httpVersion,
            String body
    ) {
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.queryParams = queryParams;
        this.httpVersion = httpVersion;
        this.body = body;
    }

    @Override
    public HttpMethod getMethod() {
        return method;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public HttpHeader getHeaders() {
        return headers;
    }

    @Override
    public HttpQueryParam getQueryParams() {
        return queryParams;
    }

    @Override
    public String getHttpVersion() {
        return httpVersion;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public <T> T parseBody(Class<T> clazz) {
        return JsonParser.fromStringToObject(getBody(), clazz);
    }
}
