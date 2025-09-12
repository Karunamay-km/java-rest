package org.karunamay.core.internal;

import org.karunamay.core.api.http.HttpHeader;
import org.karunamay.core.api.http.HttpMethod;
import org.karunamay.core.api.http.HttpQueryParam;
import org.karunamay.core.api.http.HttpRequest;

class HttpRequestBuilder {

    private HttpMethod method;
    private String path;
    private HttpHeader headers;
    private HttpQueryParam queryParams;
    private byte[] body;

    public HttpRequestBuilder withMethod(HttpMethod httpMethod) {
        this.method = httpMethod;
        return this;
    }

    public HttpRequestBuilder withPath(String path) {
        this.path = path;
        return this;
    }

    public HttpRequestBuilder withHeaders(HttpHeader headers) {
        this.headers = headers;
        return this;
    }

    public HttpRequestBuilder withQueryParams(HttpQueryParam queryParams) {
        this.queryParams = queryParams;
        return this;
    }

    public HttpRequestBuilder withBody(byte[] body) {
        this.body = body;
        return this;
    }

    public HttpRequest build() {
        return new HttpRequestImpl(method, path, headers, queryParams, body);
    }
}
