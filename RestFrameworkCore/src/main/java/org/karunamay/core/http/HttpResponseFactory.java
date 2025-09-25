package org.karunamay.core.http;

public class HttpResponseFactory {
    public static <T> RestHttpResponse<T> createErrorResponse(
            int code, String message, T details) {
        return new HttpErrorResponse<>(code, message, details);
    }

    public static <T> RestHttpResponse<T> createSuccessResponse(
            int code, String message, T details) {
        return new HttpSuccessResponse<>(code, message, details);
    }
}
