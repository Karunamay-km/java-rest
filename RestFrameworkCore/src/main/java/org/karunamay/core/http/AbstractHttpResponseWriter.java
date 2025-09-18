package org.karunamay.core.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.karunamay.core.api.http.*;
import org.karunamay.core.utils.ReflectiveDTO;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class AbstractHttpResponseWriter {
    protected static <T> void responseSend(T body, HttpStatus status, ApplicationContext context) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.setDefaultPropertyInclusion(JsonInclude.Include.USE_DEFAULTS);
        String bodyJsonString = mapper.writeValueAsString(ReflectiveDTO.createDto(body));

        HttpResponse response = new HttpResponseBuilder()
                .withHttpVersion(context.getRequest().getHttpVersion())
                .withStatus(status)
                .withResponsePhrase(status.getResponsePhrase())
                .withHeaders(context.getResponseHeader())
                .withBody(bodyJsonString)
                .build();

        context.getOutputStream().write(response.toString().getBytes(StandardCharsets.UTF_8));
        context.getOutputStream().flush();
    }
}
