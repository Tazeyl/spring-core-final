package sokolov.spring.operation;

import org.springframework.stereotype.Component;
import sokolov.spring.service.AccountService;
import sokolov.spring.utils.CommonScanner;

@Component
public class AccountTransferCommand implements OperationCommand {

    private final CommonScanner commonScanner;
    private final AccountService accountService;

    public AccountTransferCommand(CommonScanner commonScanner, AccountService accountService) {
        this.commonScanner = commonScanner;
        this.accountService = accountService;
    }

    @Override
    public void execute() {
        System.out.println("Enter source account ID:\n");
        Long sourceAccountId = commonScanner.getLong("source account id");
        System.out.println("Enter target account ID:\n");
        Long targetAccountId = commonScanner.getLong("target account id");
        System.out.println("Enter amount to transfer:\n\n");
        Integer money = commonScanner.getInteger("amount");

        accountService.transfer(sourceAccountId, targetAccountId, money);
        System.out.println("Amount " + money + " transferred from account ID " + sourceAccountId + " to account ID " + targetAccountId + ".");
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_TRANSFER;
    }
}
