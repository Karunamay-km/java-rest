package org.karunamay.core.internal;

import org.karunamay.core.Error.HttpError;
import org.karunamay.core.api.config.ConfigManager;
import org.karunamay.core.api.http.ApplicationContext;
import org.karunamay.core.api.http.HttpRequest;
import org.karunamay.core.middleware.AuthenticationMiddleware;
import org.karunamay.core.middleware.MiddlewareHandler;

import org.karunamay.core.middleware.PermissionMiddleware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;


class ConnectionHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionHandler.class);
    private final Socket clientSocket;
    private final Consumer<Socket> onCloseCallback;

    public ConnectionHandler(Socket clientSocket, Consumer<Socket> onCloseCallback) {
        this.clientSocket = clientSocket;
        this.onCloseCallback = onCloseCallback;
    }

    @Override
    public void run() {
        try (
                InputStream inputStream = clientSocket.getInputStream();
                OutputStream outputStream = clientSocket.getOutputStream();
        ) {
            logger.info("Handling client {}", clientSocket.getRemoteSocketAddress());

            while (true) {
                HttpRequest httpRequest = HttpRequestParser.parse(inputStream);


                ApplicationContext context = new ApplicationContextImpl();
                context.setOutputStream(outputStream);

                if (httpRequest == null) {
                    HttpError.badRequest400(context);
                } else {
                    context.setHttpRequest(httpRequest);
                    context.setResponseHeader(httpRequest.getHeaders());
                    context.setConfigManager(ConfigManager.getInstance());
                    context.setRequestMiddlewares(List.of(
                            new AuthenticationMiddleware(),
                            new PermissionMiddleware()
                    ));
                    context.setRequestMiddlewares(ConfigManager.getInstance().getRequestMiddlewares());
                    context.setResponseMiddlewares(ConfigManager.getInstance().getResponseMiddlewares());

                    boolean continueChain = MiddlewareHandler.executePipeline(context, context.getRequestMiddlewares());


                    if (!continueChain && !context.isResponseWritten()) {
                        MiddlewareHandler.terminateExecution(context);
                    }
                    if (continueChain) HttpRequestDispatcher.dispatch(context);

                    Optional<String> connectionHeader = httpRequest.getHeaders().get("Connection");
                    if (connectionHeader.isPresent() && connectionHeader.get().equals("close")) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            logger.warn("Connection error with {}: {}", clientSocket.getRemoteSocketAddress(), e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error while handling client {}", clientSocket.getRemoteSocketAddress(), e);
            throw new RuntimeException(e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                onCloseCallback.accept(clientSocket);
                logger.info("Closed connection with {}", clientSocket.getRemoteSocketAddress());
            }
        }
    }
}
