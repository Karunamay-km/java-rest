package org.karunamay.core.api.http;

import org.karunamay.core.api.router.PathParameter;

import java.io.OutputStream;
import java.util.Map;

public interface ApplicationContext {

    HttpRequest getRequest();

    void setHttpRequest(HttpRequest httpRequest);

    OutputStream getOutputStream();

    void setOutputStream(OutputStream outputStream);

    HttpHeader getResponseHeader();

    void setResponseHeader(HttpHeader responseHeader);

    Map<String, PathParameter> getPathParameters();

    void setPathParameter(Map<String, PathParameter> pathParameter);
}
