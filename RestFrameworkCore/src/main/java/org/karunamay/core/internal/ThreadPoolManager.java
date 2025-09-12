package org.karunamay.core.internal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ThreadPoolManager {

    private final ExecutorService executorService;

    public ThreadPoolManager(int nThread) {
        this.executorService = Executors.newFixedThreadPool(nThread);
    }

    public void submit(Runnable task) {
        executorService.submit(task);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
