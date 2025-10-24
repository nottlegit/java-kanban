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
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.nio.charset.StandardCharsets;

public abstract class BaseHandlersTasks implements HttpHandler {
    protected final TaskManager manager;
    protected final Gson gson;
    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public BaseHandlersTasks(TaskManager manager) {
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
                    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd:MM:yyyy mm:ss");

                    @Override
                    public void write(final JsonWriter jsonWriter,
                                      final LocalDateTime localDateTime) throws IOException {
                        jsonWriter.value(localDateTime.format(dtf));
                    }

                    @Override
                    public LocalDateTime read (final JsonReader jsonReader) throws IOException {
                        return LocalDateTime.parse(
                                jsonReader.nextString(),
                                dtf
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

    protected void sendText(HttpExchange exchange,
                            String responseString,
                            int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }

    protected void sendInvalidId(HttpExchange exchange) throws IOException {
        String responseString = "Некорректный идентификатор поста";
        int responseCode = 400;

        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        String responseString = "Объект не найден";
        int responseCode = 404;

        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }

    abstract void handlerGet(HttpExchange httpExchange) throws IOException;
}
