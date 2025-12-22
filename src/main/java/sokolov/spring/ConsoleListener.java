package sokolov.spring;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import sokolov.spring.service.OperationsConsoleListener;

@Component
public class ConsoleListener {

    private final OperationsConsoleListener operationsConsoleListener;
    private Thread consoleListenerThread;

    public ConsoleListener(OperationsConsoleListener operationsConsoleListener) {
        this.operationsConsoleListener = operationsConsoleListener;
    }

    @PostConstruct
    public void postConstruct() {
        this.consoleListenerThread = new Thread(() -> {
            operationsConsoleListener.start();
            operationsConsoleListener.listenUpdates();
        });
        consoleListenerThread.start();
    }

    @PreDestroy
    public void preDestroy() {
        consoleListenerThread.interrupt();
        operationsConsoleListener.endListen();
    }
}
