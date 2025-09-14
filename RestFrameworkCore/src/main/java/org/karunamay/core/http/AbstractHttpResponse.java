package org.karunamay.core.http;

import org.karunamay.core.api.http.HttpHeader;
import org.karunamay.core.api.http.HttpStatus;

public abstract class AbstractHttpResponse {
    protected String httpVersion;
    protected HttpStatus status;
    protected String responsePhrase;
    protected HttpHeader headers;
    protected String body;
}
