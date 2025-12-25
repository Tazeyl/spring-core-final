package sokolov.spring.utils;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class MoneyUtils {

    public static BigDecimal safeAmount(String amountStr) {
        try {

            BigDecimal amount = new BigDecimal(amountStr);
            if (amount.scale() > 4) {
                throw new IllegalArgumentException("Scale too large");
            }
            return amount.setScale(2, RoundingMode.HALF_EVEN);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Input not BigDecimal");
        }
    }
}
