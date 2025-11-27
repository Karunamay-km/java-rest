package org.karunamay.core.internal;

import lombok.Getter;
import org.karunamay.core.api.http.*;
import org.karunamay.core.exception.BadHttpRequestException;
import org.karunamay.core.http.HttpHeaderFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
class HttpRequestParser {

    private HttpMethod method;
    private String path;
    private HttpHeader headers;
    private HttpQueryParam queryParams;
    private String httpVersion;
    private String body;

    public HttpRequest parse(InputStream inputStream) throws Exception {
        BufferedReader bf = getBufferedReader(inputStream);
        RequestLine requestLine = new RequestLine(getRequestLine(bf)).initRequestLineObject();
        if (requestLine != null) {
            method = HttpMethod.valueOf(requestLine.getMethod());
            path = requestLine.getUri().getPath();
            headers = headerParser(bf);
            queryParams = queryParamParser(requestLine.getUri());
            httpVersion = requestLine.getHttpVersion();
            body = bodyParser(bf).toString();

            return new HttpRequestBuilder()
                    .withMethod(method)
                    .withPath(path)
                    .withHeaders(headers)
                    .withQueryParams(queryParams)
                    .withHttpVersion(httpVersion)
                    .withBody(body)
                    .build();
        }

        return null;
    }


    private static BufferedReader getBufferedReader(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    private static String getRequestLine(BufferedReader bf) throws IOException {
        return bf.readLine();
    }


    private static class RequestLine {

        private final String line;
        @Getter
        private String method;
        @Getter
        private String httpVersion;
        @Getter
        private URI uri;

        RequestLine(String line) {
            this.line = line;
        }

        public RequestLine initRequestLineObject() throws URISyntaxException {
            if (this.line != null) {
                String[] requestLine = line.split(" ");
                this.method = requestLine[0];
                this.uri = new URI(requestLine[1]);
                this.httpVersion = requestLine[2];

                return this;
            }
            return null;
        }
    }

    private HttpHeader headerParser(BufferedReader bf) throws BadHttpRequestException, IOException {
        String line;
        HttpHeader headers = new HttpHeaderFactory().getHeaders();
        while ((line = bf.readLine()) != null && !line.isEmpty()) {
            String[] headerLine = line.split(":", 2);
            if (headerLine.length == 2)
                headers.set(headerLine[0], headerLine[1].trim());
            else
                throw new BadHttpRequestException("Incorrect HTTP headers format");
        }
        return headers;
    }

    private String bodyParser(BufferedReader bf) throws Exception {
        if (getHeaders().asMap().containsKey("Content-Length")) {
            Optional<String> contentLengthHeader = getHeaders().get("Content-Length");
            if (contentLengthHeader.isPresent() &&
                    !contentLengthHeader.get().isEmpty() &&
                    !contentLengthHeader.get().trim().equals(Integer.toString(0))
            ) {
                int contentLength = Integer.parseInt(contentLengthHeader.get());
                char[] buffer = new char[contentLength];
                int totalRead = 0;
                while (totalRead < contentLength) {
                    int read = bf.read(buffer, totalRead, contentLength - totalRead);
                    if (read == -1) break;
                    totalRead += read;
                }
                return new String(buffer);
            }
        }
        return "";
    }

    private static HttpQueryParam queryParamParser(URI uri) {

        String queryString = uri.getQuery();
        List<String> queryList = new ArrayList<>();
        HttpQueryParam queryParam = new HttpQueryParamImpl();
        if (queryString != null) {
            if (queryString.contains("&")) {
                queryList.addAll(List.of(queryString.split("&")));
                queryList.forEach(query -> {
                    if (query.contains("=")) {
                        String[] parameter = query.split("=", 2);
                        queryParam.set(
                                parameter[0],
                                parameter[1]
                        );
                    } else {
                        if (!query.isEmpty()) {
                            queryParam.set(query, "true");
                        }
                    }
                });
            } else {
                if (queryString.contains("=")) {
                    queryList.addAll(List.of(queryString.split("=", 2)));
                    String key = queryList.get(0);
                    if (!key.isBlank())
                        queryParam.set(key, queryList.get(1));
                }
            }
        }

        return queryParam;
    }


}
