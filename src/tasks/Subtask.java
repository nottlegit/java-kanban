package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private final int idEpic;

    public Subtask(
            String title,
            String description,
            Status status,
            int idEpic,
            Duration duration,
            LocalDateTime localDateTime) {
        super(title, description, status, duration, localDateTime);
        this.idEpic = idEpic;
    }

    public Subtask(int id, String title, String description, Status status,
                   int idEpic, Duration duration, LocalDateTime localDateTime) {
        super(id, title, description, status, duration, localDateTime);
        this.idEpic = idEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "idEpic=" + idEpic +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }
}
