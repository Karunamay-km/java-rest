package org.karunamay.core.internal;

import org.karunamay.core.api.http.HttpHeader;
import org.karunamay.core.api.http.HttpMethod;
import org.karunamay.core.api.http.HttpQueryParam;
import org.karunamay.core.api.http.HttpRequest;
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

class HttpRequestParser {

    private static HttpMethod method;
    private static String path;
    private static HttpHeader headers;
    private static HttpQueryParam queryParams;

    public static HttpRequest parse(InputStream inputStream) throws Exception {
        BufferedReader bf = getBufferedReader(inputStream);
        RequestLine requestLine = new RequestLine(getRequestLine(bf));
        method = HttpMethod.valueOf(requestLine.getMethod());
        path = requestLine.getUri().getPath();
        headers = headerParser(bf);
        queryParams = queryParamParser(requestLine.getUri());

        return new HttpRequestBuilder()
                .withMethod(method)
                .withPath(path)
                .withHeaders(headers)
                .withQueryParams(queryParams)
                .build();
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public HttpHeader getHeaders() {
        return headers;
    }

    public HttpQueryParam getQueryParams() {
        return queryParams;
    }

    private static BufferedReader getBufferedReader(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    private static String getRequestLine(BufferedReader bf) throws IOException {
        return bf.readLine();
    }

    private static class RequestLine {

        private final String method;
        private final String httpVersion;
        private final URI uri;

        public RequestLine(String line) throws URISyntaxException {
            String[] requestLine = line.split(" ");
            this.method = requestLine[0];
            this.uri = new URI(requestLine[1]);
            this.httpVersion = requestLine[2];
        }

        public String getMethod() {
            return method;
        }

        public String getHttpVersion() {
            return httpVersion;
        }

        public URI getUri() {
            return uri;
        }
    }

    private static HttpHeader headerParser(BufferedReader bf) throws BadHttpRequestException, IOException {
        bf.readLine(); // skip the first line
        String line;
        HttpHeader headers = HttpHeaderFactory.create();
        while ((line = bf.readLine()) != null && !line.isEmpty()) {
            String[] headerLine = line.split(":", 2);
            if (headerLine.length == 2)
                headers.set(headerLine[0], headerLine[1].trim());
            else
                throw new BadHttpRequestException("Incorrect HTTP headers format");
        }
        return headers;
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
                        queryParams.set(
                                parameter[0],
                                parameter[1]
                        );
                    } else {
                        if (!query.isEmpty()) {
                            queryParams.set(query, "true");
                        }
                    }
                });
            } else {
                if (queryString.contains("=")) {
                    queryList.addAll(List.of(queryString.split("=", 2)));
                    String key = queryList.get(0);
                    if (!key.isBlank())
                        queryParams.set(key, queryList.get(1));
                }
            }
        }

        return queryParam;
    }


}
