package org.karunamay.core.internal;

import org.karunamay.core.api.http.HttpHeader;
import org.karunamay.core.api.http.HttpMethod;
import org.karunamay.core.api.http.HttpQueryParam;
import org.karunamay.core.api.http.HttpRequest;

class HttpRequestImpl implements HttpRequest {

    private final HttpMethod method;
    private final String path;
    private final HttpHeader headers;
    private final HttpQueryParam queryParams;
    private final byte[] body;

    HttpRequestImpl(
            HttpMethod method,
            String path,
            HttpHeader headers,
            HttpQueryParam queryParams,
            byte[] body
    ) {
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.queryParams = queryParams;
        this.body = body;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public HttpHeader getHeaders() {
        return headers;
    }

    public HttpQueryParam getQueryParams() {
        return queryParams;
    }

    public byte[] getBody() {
        return body;
    }
}
