package http.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.adapter.InstantAdapter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public abstract class AbstractHandler implements HttpHandler {

    protected final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();
    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    protected void writeResponse(HttpExchange httpExchange, String response, int statusCode) throws IOException {
        byte[] bytes = response.getBytes(DEFAULT_CHARSET);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=" + DEFAULT_CHARSET);
        httpExchange.sendResponseHeaders(statusCode, bytes.length);

        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(bytes);
        }
    }

}