package org.karunamay.core.http;

import org.karunamay.core.api.http.*;
import org.karunamay.core.exception.ResponseSentException;
import org.karunamay.core.middleware.MiddlewareHandler;
import org.karunamay.core.utils.JsonParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class AbstractHttpResponseWriter {
    protected static <T> void responseSend(T body, HttpStatus status, ApplicationContext context) throws IOException {
        boolean continuePipeline = MiddlewareHandler.executePipeline(context, context.getResponseMiddlewares());
        if (!continuePipeline) {
            MiddlewareHandler.terminateExecution(context);
        } else {
            String bodyJsonString = JsonParser.fromObjectToString(body);
            byte[] bytes = bodyJsonString.getBytes(StandardCharsets.UTF_8);

            HttpHeader header = context.getResponseHeader();
            header.set("Content-Length", String.valueOf(bytes.length));

            HttpResponse response = new HttpResponseBuilder()
                    .withHttpVersion(context.getHttpRequest().getHttpVersion())
                    .withStatus(status)
                    .withResponsePhrase(status.getResponsePhrase())
                    .withHeaders(context.getResponseHeader())
                    .withBody(bodyJsonString)
                    .build();

            context.getOutputStream().write(response.buildHeaderAndStatusLine().getBytes(StandardCharsets.UTF_8));
            context.getOutputStream().write(bytes);
            context.getOutputStream().flush();
            context.setResponseWritten(true);

//          Stop the execution after response has been sent
            throw new ResponseSentException();
        }
    }
}
