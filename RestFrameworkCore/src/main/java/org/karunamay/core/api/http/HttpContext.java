package org.karunamay.core.api.http;

import java.io.OutputStream;

public interface HttpContext {
    HttpRequest getRequest();
    OutputStream getOutputStream();
    HttpHeader getResponseHeader();
}
