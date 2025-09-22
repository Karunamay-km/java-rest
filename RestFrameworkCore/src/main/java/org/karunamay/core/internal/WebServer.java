package org.karunamay.core.internal;

import java.io.IOException;

public class WebServer {

    private final int PORT;
    private final ThreadPoolManager threadPoolManager;
    private ConnectionListener connectionListener;

    public WebServer(int PORT, int threadPoolSize) {
        this.PORT = PORT;
        this.threadPoolManager = new ThreadPoolManager(threadPoolSize);
    }

    public void start() throws IOException {
        connectionListener = new ConnectionListener(PORT, threadPoolManager);
        connectionListener.start();

    }

    public void stop() {
        connectionListener.stop();
        threadPoolManager.shutdown();
    }
}
