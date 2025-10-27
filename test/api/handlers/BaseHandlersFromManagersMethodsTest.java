package api.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseHandlersFromManagersMethodsTest extends HttpTaskServerTest {
    protected Gson gson;

    @BeforeEach
    @Override
    void setUp() throws IOException {
        super.setUp();

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

        Epic epic1 = new Epic("Test Epic № 1", "Test Description", Status.NEW);
        Task task1 = new Task("Test Task № 1", "Test Description № 1",
                Status.NEW, Duration.ofMinutes(120), LocalDateTime.now().minus(Duration.ofDays(1)));
        Task task2 = new Task("Test Task № 2", "Test Description № 1",
                Status.NEW, Duration.ofMinutes(120), LocalDateTime.now().minus(Duration.ofDays(2)));

        getManager().add(task1);
        getManager().add(task2);
        getManager().add(epic1);

        Subtask subtask1 = new Subtask("Test Subtask № 1", "Test Description № 1",
                Status.NEW, epic1.getId(), Duration.ofMinutes(120), LocalDateTime.now().minus(Duration.ofDays(3)));
        Subtask subtask2 = new Subtask("Test Subtask № 2", "Test Description № 1",
                Status.NEW, epic1.getId(), Duration.ofMinutes(120), LocalDateTime.now().minus(Duration.ofDays(4)));

        getManager().add(subtask1);
        getManager().add(subtask2);

        getManager().getEpicById(epic1.getId());
        getManager().getTaskById(task1.getId());
        getManager().getTaskById(task2.getId());
        getManager().getSubtaskById(subtask1.getId());
        getManager().getSubtaskById(subtask2.getId());
        getManager().getEpicById(epic1.getId());
    }
}
