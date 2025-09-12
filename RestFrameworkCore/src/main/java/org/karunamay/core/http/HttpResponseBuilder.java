package org.karunamay.core.http;

import org.karunamay.core.api.http.HttpHeader;

class HttpResponseBuilder {

    private HttpStatus status;
    private String responsePhrase;
    private String httpVersion;
    private HttpHeader headers;
    private String body;

    public HttpResponseBuilder withStatus(HttpStatus status) {
        this.status = status;
        return this;
    }

    public HttpResponseBuilder withResponsePhrase(String responsePhrase) {
        this.responsePhrase = responsePhrase;
        return this;
    }

    public HttpResponseBuilder withHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
        return this;
    }

    public HttpResponseBuilder withHeaders(HttpHeader headers) {
        this.headers = headers;
        return this;
    }

    public HttpResponseBuilder withBody(String body) {
        this.body = body;
        return this;
    }

    public HttpResponseImpl build() {
        return new HttpResponseImpl(httpVersion, status, responsePhrase, body, headers);
    }

}
