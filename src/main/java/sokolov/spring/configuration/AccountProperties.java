package sokolov.spring.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sokolov.spring.utils.MoneyUtils;

import java.math.BigDecimal;

@Component
public class AccountProperties {

    @Value("${account.default-amount}")
    private String defaultAmount;

    @Value("${account.transfer-commission}")
    private String transferCommission;

    public BigDecimal getDefaultAmount() {
        return MoneyUtils.safeAmount(defaultAmount);
    }

    public BigDecimal getTransferCommission() {
        return MoneyUtils.safeAmount(transferCommission);
    }
}
