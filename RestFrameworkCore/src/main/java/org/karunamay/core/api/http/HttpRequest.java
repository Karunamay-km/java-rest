package org.karunamay.core.api.http;

public interface HttpRequest {
    HttpMethod getMethod();
    String getPath();
    HttpHeader getHeaders();
    HttpQueryParam getQueryParams();
    String getHttpVersion();

    String getBody();

    <T> T parseBody(Class<T> clazz);
}
