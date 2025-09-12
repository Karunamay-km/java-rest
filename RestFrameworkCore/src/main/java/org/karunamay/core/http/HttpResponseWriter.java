package org.karunamay.core.http;

import org.karunamay.core.api.http.HttpContext;
import org.karunamay.core.api.http.HttpHeader;
import org.karunamay.core.api.http.HttpResponse;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HttpResponseWriter {

//    private final HttpContext context;
    private final HttpHeader headers = HttpHeaderFactory.create();
//
//    public HttpResponseWriter(HttpContext context) {
//        this.context = context;
//    }

    public void setHeaders(HttpHeader headers) {
        headers.forEach(this.headers::set);
    }

    public HttpResponse write(String htmlPath, HttpStatus status) {
        OutputStream outputStream = this.context.getOutputStream();
        HttpResponse httpResponseImpl;

        try (InputStream inputStream = getClass().getResourceAsStream("/" + htmlPath)) {
            System.out.println("inputStream" + inputStream);
            if (inputStream == null) {
                throw new FileNotFoundException(String.format("%s not found", htmlPath));
            }
            String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            HttpResponseBuilder httpResponseBuilder = new HttpResponseBuilder();
            httpResponseImpl = httpResponseBuilder
                    .withStatus(status)
                    .withResponsePhrase(status.getResponsePhrase())
                    .withHeaders(this.headers)
                    .withBody(content)
                    .build();
            outputStream.write(httpResponseImpl.toString().getBytes());
            outputStream.write(httpResponseImpl.getBody().getBytes());
            outputStream.flush();
            return httpResponseImpl;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
