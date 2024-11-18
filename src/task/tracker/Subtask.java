package task.tracker;

public class Subtask extends Task{
    private final int idEpic;

    public Subtask(String title, int idEpic) {
        super(title);
        this.idEpic = idEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }
}
