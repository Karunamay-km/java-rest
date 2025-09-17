package org.karunamay.core.internal;

import org.karunamay.core.api.http.HttpContext;
import org.karunamay.core.api.http.HttpRequest;
import org.karunamay.core.api.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

class ConnectionHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionHandler.class);
    private final Socket clientSocket;

    public ConnectionHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                InputStream inputStream = clientSocket.getInputStream();
                OutputStream outputStream = clientSocket.getOutputStream();
        ) {
            logger.info("Handling client {}", clientSocket.getRemoteSocketAddress());

            HttpRequest httpRequest = HttpRequestParser.parse(inputStream);
            HttpContext context = new HttpContextImpl(httpRequest, outputStream);
            HttpRequestDispatcher.dispatch(context);

            logger.info("Closing connection with {}", clientSocket.getRemoteSocketAddress());
            clientSocket.close();

        } catch (IOException e) {
            logger.warn("Connection error with {}: {}", clientSocket.getRemoteSocketAddress(), e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
