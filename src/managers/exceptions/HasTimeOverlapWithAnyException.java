package managers.exceptions;

public class HasTimeOverlapWithAnyException extends RuntimeException {
    public HasTimeOverlapWithAnyException() {
        super("Задача не добавлена, есть пересечение");
    }
}
