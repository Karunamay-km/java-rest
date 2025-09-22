package org.karunamay.core.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.RejectedExecutionException;

class ConnectionListener {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionListener.class);

    private final int PORT;
    private final ThreadPoolManager threadPoolManager;
    private volatile boolean running = false;
    private ServerSocket serverSocket;

    private final Set<Socket> activeConnections = Collections.synchronizedSet(new HashSet<>());

    public ConnectionListener(int PORT, ThreadPoolManager threadPoolManager) {
        this.PORT = PORT;
        this.threadPoolManager = threadPoolManager;
    }

    public void start() throws IOException {

        running = true;

        serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("127.0.0.1", PORT));

        logger.info("Server started at {}", PORT);

        while (running) {
            try {
                Socket socket = serverSocket.accept();
                socket.setSoTimeout(30_000);
                activeConnections.add(socket);

                logger.info("Accept new connection from {}", socket.getRemoteSocketAddress());
                ConnectionHandler connectionHandler = new ConnectionHandler(socket, this::onConnectionClosed);

                try {
                    threadPoolManager.submit(connectionHandler);
                } catch (RejectedExecutionException e) {
                    logger.warn("Thread pool is full. Rejecting connection from {}", socket.getRemoteSocketAddress());
                    socket.close();
                }

            } catch (IOException e) {
                if (running) {
                    logger.error("Error while accepting connection", e);
                } else {
                    logger.info("Server shutting down ...");
                }
            }
        }
    }

    public void stop() {
        running = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
                logger.info("Server stopped.");
            }
        } catch (IOException e) {
            logger.error("Error while closing server socket", e);
        }
        synchronized (this.activeConnections) {
            for (Socket socket : activeConnections) {
                try {
                    socket.close();
                } catch (Exception e) {
                    logger.warn("failed to close client socket {} ", socket.getRemoteSocketAddress(), e);
                }

            }
            activeConnections.clear();
        }

        threadPoolManager.shutdown();
        logger.info("Server listener stopped");

    }

    public void onConnectionClosed(Socket socket) {
        this.activeConnections.remove(socket);
    }

}
