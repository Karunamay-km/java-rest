package org.karunamay.core.api.http;

public interface HttpResponse {


    String getHttpVersion();

    HttpStatus getStatus();

    String getResponsePhrase();

    HttpHeader getHeaders();

    String getBody();

    String buildHeaderAndStatusLine();
}
