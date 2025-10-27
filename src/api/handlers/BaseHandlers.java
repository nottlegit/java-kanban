package api.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.nio.charset.StandardCharsets;

public abstract class BaseHandlers implements HttpHandler {
    protected final TaskManager manager;
    protected final Gson gson;
    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public BaseHandlers(TaskManager manager) {
        this.manager = manager;
        gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new TypeAdapter<Duration>() {
                    @Override
                    public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
                        jsonWriter.value(duration.toMinutes());
                    }

                    @Override
                    public Duration read (final JsonReader jsonReader) throws IOException {
                        return Duration.ofMinutes(jsonReader.nextLong());
                    }
                })
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapter<LocalDateTime>() {
                    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm");

                    @Override
                    public void write(final JsonWriter jsonWriter,
                                      final LocalDateTime localDateTime) throws IOException {
                        if (localDateTime == null) {
                            jsonWriter.nullValue();
                        } else {
                            jsonWriter.value(localDateTime.format(formatter));
                        }
                    }

                    @Override
                    public LocalDateTime read (final JsonReader jsonReader) throws IOException {
                        return LocalDateTime.parse(
                                jsonReader.nextString(),
                                formatter
                        );
                    }
                })
                .create();
    }

    protected Optional<Integer> getTheIdFromThePath(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(pathParts[2]));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    protected void send(HttpExchange exchange,
                      byte[] responseBytes,
                      int responseCode) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(responseCode, responseBytes.length);
        exchange.getResponseBody().write(responseBytes);
        exchange.close();
    }

    protected void sendText(HttpExchange exchange, String responseString) throws IOException {
        int responseCode = 200;
        byte[] responseBytes = responseString.getBytes(DEFAULT_CHARSET);

        send(exchange,responseBytes, responseCode);
    }

    protected void sendInvalidId(HttpExchange exchange) throws IOException {
        byte[] responseBytes = "Invalid ID".getBytes(DEFAULT_CHARSET);
        int responseCode = 400;

        send(exchange,responseBytes, responseCode);
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        byte[] responseBytes = "Not Found".getBytes(DEFAULT_CHARSET);
        int responseCode = 404;

        send(exchange,responseBytes, responseCode);
    }

    abstract void handlerGet(HttpExchange httpExchange) throws IOException;
}
