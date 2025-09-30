package org.karunamay.core.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.karunamay.core.api.http.*;
import org.karunamay.core.exception.ResponseSentException;
import org.karunamay.core.middleware.MiddlewareHandler;
import org.karunamay.core.utils.ReflectiveDTO;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class AbstractHttpResponseWriter {
    protected static <T> void responseSend(T body, HttpStatus status, ApplicationContext context) throws IOException {
        boolean continuePipeline = MiddlewareHandler.executePipeline(context, context.getResponseMiddlewares());
        if (!continuePipeline) {
            MiddlewareHandler.terminateExecution(context);
        } else {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDefaultPropertyInclusion(JsonInclude.Include.USE_DEFAULTS);
            String bodyJsonString = mapper.writeValueAsString(ReflectiveDTO.createDto(body));
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

            throw new ResponseSentException();
        }
    }

    protected static <T> void badResponseSend(ApplicationContext context) throws Exception {
//        TODO: Refactor as above
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDefaultPropertyInclusion(JsonInclude.Include.USE_DEFAULTS);

        HttpResponse response = new HttpResponseBuilder()
                .withHttpVersion("HTTP/1.1")
                .withStatus(HttpStatus.HTTP_BAD_REQUEST)
                .withResponsePhrase(HttpStatus.HTTP_BAD_REQUEST.getResponsePhrase())
                .withHeaders(HttpHeaderFactory.create())
                .build();

        context.getOutputStream().write(response.toString().getBytes(StandardCharsets.UTF_8));
        context.getOutputStream().flush();
        context.setResponseWritten(true);
    }

}
