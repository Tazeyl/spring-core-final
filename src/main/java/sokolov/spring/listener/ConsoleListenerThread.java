package sokolov.spring.listener;

import java.util.Scanner;

public class ConsoleListenerThread extends Thread {
    private volatile boolean running = true;
    private final Scanner scanner;

    public ConsoleListenerThread() {
        this.scanner = new Scanner(System.in);
        this.setName("Console-Listener-Thread");
        this.setDaemon(true); // Демон-поток (завершится с main)
    }

    @Override
    public void run() {
        System.out.println("Консольный слушатель запущен. Введите команды:");

        while (running && scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();

            if ("exit".equalsIgnoreCase(input)) {
                System.out.println("Завершение работы...");
                stopListener();
                break;
            }

            if (!input.isEmpty()) {
                processCommand(input);
            }
        }

        scanner.close();
        System.out.println("Консольный слушатель остановлен.");
    }

    private void processCommand(String command) {
        System.out.println("Обработка команды: " + command);
        // Ваша логика обработки команд
    }

    public void stopListener() {
        running = false;
        this.interrupt();
    }
}
