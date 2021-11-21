package com.revature.banking.screens.bank;

import com.revature.banking.exceptions.AuthenticationException;
import com.revature.banking.exceptions.InvalidRequestException;
import com.revature.banking.models.BankAccount;
import com.revature.banking.screens.Screen;
import com.revature.banking.services.BankService;
import com.revature.banking.util.ScreenRouter;

import java.io.BufferedReader;

public class OpenAccountScreen extends Screen {

    private final BankService bankService;

    public OpenAccountScreen(BufferedReader consoleReader, ScreenRouter router, BankService bankService) {
        super("LoginScreen", "/open_bank_account", consoleReader, router);
        this.bankService = bankService;
    }

    @Override
    public void render() throws Exception {

        String menu =
                "\nPlease provide which kind of bank account you want to open.\n" +
                        "1) Checking Account\n" +
                        "2) Saving Account\n" +
                        "3) Exit this menu\n" +
                        "> ";
        String accountType = "";
        boolean bContinue = false;
        do {
            System.out.print(menu);
            String accountTypeSelection = consoleReader.readLine();
            switch (accountTypeSelection) {
                case "1":
                    accountType = "Checking";
                    break;
                case "2":
                    accountType = "Saving";
                    break;
                case "3":
                    router.navigate("/dashboard");
                    break;
                default:
                    bContinue=true;
                    System.out.println("You have made an invalid selection");
            }
        }
        while (bContinue);

        System.out.print("\nWhat name do you want to call it?\n" +
                "> ");

        String accountName = consoleReader.readLine();

        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountType(accountType);
        bankAccount.setAccountName(accountName);

        try {
            bankService.openBankAccount(bankAccount);
            logger.info("Successful account opened!");
            router.navigate("/dashboard");
        } catch (InvalidRequestException | AuthenticationException e) {
            System.out.println(e.getMessage());
        }
    }
}
