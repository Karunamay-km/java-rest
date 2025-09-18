package org.karunamay.core.Error;

import org.karunamay.core.api.http.HttpStatus;

public enum Error {
    NOT_FOUND(HttpStatus.HTTP_NOT_FOUND),
    SERVER_ERROR(HttpStatus.HTTP_SERVER_ERROR);

    final HttpStatus status;

    Error(HttpStatus status) {
        this.status = status;
    }



}
