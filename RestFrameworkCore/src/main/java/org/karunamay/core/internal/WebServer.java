package org.karunamay.core.internal;

import org.karunamay.core.api.router.RouteRegistry;
import org.karunamay.core.api.router.RouterConfig;

import java.io.IOException;
import java.util.ServiceLoader;

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
