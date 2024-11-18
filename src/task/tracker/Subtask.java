package task.tracker;

import java.util.Objects;

public class Subtask extends Task{
    private final int idEpic;

    public Subtask(String title, int idEpic) {
        super(title);
        this.idEpic = idEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idEpic);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "idEpic=" + idEpic +
                ", title='" + title + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
