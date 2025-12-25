package sokolov.spring.utils;

import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Scanner;

@Component
public class CommonScanner {

    private final Scanner scanner;


    public CommonScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public Long getLong(String param) {
        try {
            Long longId = scanner.nextLong();
            scanner.nextLine(); // Очищаем буфер!
            return longId;
        } catch (Exception ex) {
            throw new IllegalArgumentException(String.format("%s must be integer", param));
        }
    }

    public String getString(String param) {
        try {
            String nextLine = scanner.nextLine();
            if (nextLine.isBlank()) {
                throw new IllegalArgumentException(String.format("%s must be not blank", param));
            }
            return nextLine;
        } catch (NoSuchElementException ex) {
            throw new IllegalArgumentException(String.format("%s must be string", param));
        }
    }

    /**
     * Use in console listener without check
     */
    public String nextLine() {
        return scanner.nextLine();
    }

}
