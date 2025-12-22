package sokolov.spring.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AccountProperties {

    @Value("${account.default-amount}")
    private int defaultAmount;

    @Value("${account.transfer-commission}")
    private double transferCommission;

    public int getDefaultAmount() {
        return defaultAmount;
    }

    public double getTransferCommission() {
        return transferCommission;
    }
}
