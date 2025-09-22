package org.karunamay.core.http;

import org.karunamay.core.api.http.HttpHeader;
import org.karunamay.core.api.http.HttpResponse;
import org.karunamay.core.api.http.HttpStatus;

class HttpResponseImpl extends AbstractHttpResponse implements HttpResponse {

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

    @Override
    public String getHttpVersion() {
        return this.httpVersion;
    }

    @Override
    public HttpStatus getStatus() {
        return this.status;
    }

    @Override
    public String getResponsePhrase() {
        return this.responsePhrase;
    }

    @Override
    public HttpHeader getHeaders() {
        return this.headers;
    }

    @Override
    public String getBody() {
        return this.body;
    }

    @Override
    public String buildHeaderAndStatusLine() {
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

        response.append("\r\n");
        return response.toString();
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
