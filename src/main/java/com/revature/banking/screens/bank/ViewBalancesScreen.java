package com.revature.banking.screens.bank;

import com.revature.banking.exceptions.AuthenticationException;
import com.revature.banking.exceptions.InvalidRequestException;
import com.revature.banking.models.BankAccount;
import com.revature.banking.screens.Screen;
import com.revature.banking.services.BankService;
import com.revature.banking.util.ScreenRouter;

import java.io.BufferedReader;
import java.text.NumberFormat;
import java.util.List;

public class ViewBalancesScreen extends Screen {

    private final BankService bankService;

    public ViewBalancesScreen(BufferedReader consoleReader, ScreenRouter router, BankService bankService) {
        super("LoginScreen", "/view_balances", consoleReader, router);
        this.bankService = bankService;
    }

    @Override
    public void render() throws Exception {

        StringBuilder menu =
                new StringBuilder("\nView the balance of my account(s)\n");

        try {
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            List<BankAccount> bankAccountLists = bankService.getBankAccountsByUserId();
            if (bankAccountLists.size() == 0) {
                System.out.println("You don't have an account.");
                router.navigate("/dashboard");
            }

            for (int i = 0; i < bankAccountLists.size(); i++) {
                menu.append(i + 1);
                menu.append(") ");
                menu.append(bankAccountLists.get(i).getAccountType());
                menu.append(" - ");
                menu.append(bankAccountLists.get(i).getAccountName());
                menu.append(" - ");
                menu.append(formatter.format(bankAccountLists.get(i).getBalance()));
                menu.append("\n");
            }

            System.out.print(menu);

            router.navigate("/dashboard");

        } catch (InvalidRequestException | AuthenticationException e) {
            System.out.println(e.getMessage());
        }
    }
}
