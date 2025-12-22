package sokolov.spring.operation;

public interface OperationCommand {

    void execute();

    ConsoleOperationType getOperationType();
}
