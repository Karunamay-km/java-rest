package org.karunamay.core.http;

import org.karunamay.core.api.http.HttpHeader;
import org.karunamay.core.api.http.HttpResponse;
import org.karunamay.core.api.http.HttpStatus;

class HttpResponseImpl implements HttpResponse {

    private final String httpVersion;
    private final HttpStatus status;
    private final String responsePhrase;
    private final HttpHeader headers;
    private final String body;

    public HttpResponseImpl(
            String httpVersion,
            HttpStatus status,
            String responsePhrase,
            String body,
            HttpHeader headers
    ) {
        this.headers = headers;
        this.httpVersion = httpVersion;
        this.status = status;
        this.responsePhrase = responsePhrase;
        this.body = body;
    }

    public String getHttpVersion() {
        return this.httpVersion;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public String getResponsePhrase() {
        return this.responsePhrase;
    }

    public HttpHeader getHeaders() {
        return this.headers;
    }

    public String getBody() {
        return this.body;
    }

    @Override
    public String toString() {
        StringBuilder response = new StringBuilder();

        response.append(this.httpVersion)
                .append(" ")
                .append(this.status.getCode())
                .append(" ")
                .append(this.responsePhrase)
                .append("\r\n");

        this.headers.asMap().forEach((k, v) -> {
            response.append(k)
                    .append(": ")
                    .append(v)
                    .append("\r\n");
        });

        response.append("\r\n\r\n");
        response.append(this.body);
        return response.toString();
    }
}
