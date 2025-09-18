package org.karunamay.core.http;

import java.time.LocalDateTime;

public abstract class BaseHttpResponseJson {

    private boolean success;
    private LocalDateTime timestamp;
    private String path;

}
