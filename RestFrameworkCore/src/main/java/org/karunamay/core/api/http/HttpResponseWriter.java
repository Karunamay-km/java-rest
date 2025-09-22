package org.karunamay.core.api.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.karunamay.core.exception.HttpResponseException;
import org.karunamay.core.http.AbstractHttpResponseWriter;
import org.karunamay.core.http.HttpHeaderFactory;
import org.karunamay.core.http.HttpResponseBuilder;
import org.karunamay.core.utils.ReflectiveDTO;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HttpResponseWriter extends AbstractHttpResponseWriter {
    public static <T> void send(T body, HttpStatus status, ApplicationContext context) {
        try {
            responseSend(body, status, context);
        } catch (IOException e) {
            throw new HttpResponseException(e.getMessage());
        }
    }

    public static <T> void badSend(ApplicationContext context) throws Exception {
        badResponseSend(context);
    }


    public static <T> void sendServerError(ApplicationContext context, T body) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDefaultPropertyInclusion(JsonInclude.Include.USE_DEFAULTS);
        String bodyJsonString = mapper.writeValueAsString(ReflectiveDTO.createDto(body));
        byte[] bytes = bodyJsonString.getBytes(StandardCharsets.UTF_8);

        HttpHeader header = context.getResponseHeader();
        header.set("Content-Length", String.valueOf(bytes.length));

        HttpResponse response = new HttpResponseBuilder()
                .withHttpVersion("HTTP/1.1")
                .withStatus(HttpStatus.HTTP_SERVER_ERROR)
                .withResponsePhrase(HttpStatus.HTTP_SERVER_ERROR.getResponsePhrase())
                .withHeaders(HttpHeaderFactory.create())
                .withBody(bodyJsonString)
                .build();

        context.getOutputStream().write(response.toString().getBytes(StandardCharsets.UTF_8));
        context.getOutputStream().flush();
        context.setResponseWritten(true);
    }

}
