package org.karunamay.core.api.http;

public interface HttpRequest {
    HttpMethod getMethod();
    String getPath();
    HttpHeader getHeaders();
    HttpQueryParam getQueryParams();
}
