package org.karunamay.core.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.karunamay.core.api.http.*;
import org.karunamay.core.utils.ReflectiveDTO;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class AbstractHttpResponseWriter {
    protected static <T> void responseSend(T body, HttpStatus status, HttpContext context) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        String bodyJsonString = mapper.writeValueAsString(ReflectiveDTO.createDto(body));

        System.out.println("json: " + bodyJsonString);

        HttpResponse response = new HttpResponseBuilder()
                .withHttpVersion(context.getRequest().getHttpVersion())
                .withStatus(status)
                .withResponsePhrase(status.getResponsePhrase())
                .withHeaders(context.getResponseHeader())
                .withBody(bodyJsonString)
                .build();

        System.out.println(response.toString());

        context.getOutputStream().write(response.toString().getBytes(StandardCharsets.UTF_8));
        context.getOutputStream().flush();
    }
}
