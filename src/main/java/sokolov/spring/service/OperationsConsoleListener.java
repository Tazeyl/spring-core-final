package sokolov.spring.service;

import org.springframework.stereotype.Component;
import sokolov.spring.operation.ConsoleOperationType;
import sokolov.spring.operation.OperationCommand;
import sokolov.spring.utils.CommonScanner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OperationsConsoleListener {

    private final CommonScanner commonScanner;
    private final Map<ConsoleOperationType, OperationCommand> commandMap = new HashMap<>();

    public OperationsConsoleListener(List<OperationCommand> commands, CommonScanner commonScanner) {
        this.commonScanner = commonScanner;
        commands.forEach(command -> commandMap.put(command.getOperationType(), command));
    }

    public void listenUpdates() {
        while (!Thread.currentThread().isInterrupted()) {
            var operationType = listenNextOperation();
            if (operationType == null) {
                return;
            }
            processNextOperation(operationType);
        }
    }

    public void start() {
        System.out.println("Console listener started");
    }

    public void endListen() {
        System.out.println("Console listener end listen");
    }

    private ConsoleOperationType listenNextOperation() {
        System.out.println("\nPlease type next operation: ");
        printAllAvailableOperations();
        System.out.println();
        while (!Thread.currentThread().isInterrupted()) {
            var nextOperation = commonScanner.nextLine();
            try {
                return ConsoleOperationType.valueOf(nextOperation);
            } catch (IllegalArgumentException e) {
                System.out.println("No such command found");
            }
        }
        return null;
    }

    private void printAllAvailableOperations() {
        commandMap.keySet()
                .forEach(System.out::println);
    }

    private void processNextOperation(ConsoleOperationType operation) {
        try {
            var processor = commandMap.get(operation);
            processor.execute();
        } catch (Exception e) {
            System.out.printf(
                    "Error executing command %s: error=%s%n", operation,
                    e.getMessage()
            );
        }
    }
}
