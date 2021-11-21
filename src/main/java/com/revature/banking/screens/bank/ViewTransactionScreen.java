package com.revature.banking.screens.bank;

import com.revature.banking.exceptions.AuthenticationException;
import com.revature.banking.exceptions.InvalidRequestException;
import com.revature.banking.models.BankAccount;
import com.revature.banking.models.BankTransaction;
import com.revature.banking.screens.Screen;
import com.revature.banking.services.BankService;
import com.revature.banking.util.Misc;
import com.revature.banking.util.ScreenRouter;

import java.io.BufferedReader;
import java.text.NumberFormat;
import java.util.List;

public class ViewTransactionScreen extends Screen {

    private final BankService bankService;

    public ViewTransactionScreen(BufferedReader consoleReader, ScreenRouter router, BankService bankService) {
        super("LoginScreen", "/view_transaction", consoleReader, router);
        this.bankService = bankService;
    }

    @Override
    public void render() throws Exception {

        StringBuilder menu =
                new StringBuilder("\nview the transaction history for an account.\n");
        menu.append("Witch account do you want to view?\n");
        StringBuilder menu2 =
                new StringBuilder("\n\n");

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
                menu.append("\n");
            }
            menu.append(bankAccountLists.size() + 1);
            menu.append(") Exit this menu.\n");
            menu.append("> ");
            System.out.print(menu);

            String account_selected;
            int i_account_selected;
            do {
                account_selected = consoleReader.readLine();
                if (!Misc.isNumeric(account_selected)) {
                    System.out.println("You have made an invalid selection");
                    continue;
                }
                i_account_selected = Integer.parseInt(account_selected);

                if (i_account_selected == bankAccountLists.size() + 1) {
                    router.navigate("/dashboard");
                } else if (0 < i_account_selected && i_account_selected <= bankAccountLists.size()) {
                    break;
                } else {
                    System.out.println("You have made an invalid selection");
                }
            }
            while (true);

            // view the transaction history for an account
            List<BankTransaction> bankTransactions = bankService.getTransactionsByUserAccountId(bankAccountLists.get(i_account_selected - 1));
            menu2.append("Transactions of account : ").append(bankAccountLists.get(i_account_selected - 1).getAccountName()).append("\n");
            if (bankTransactions.size() == 0) {
                System.out.print(menu2);
                System.out.println("\nYou don't have a transaction.");
            } else {
                for (int i = 0; i < bankTransactions.size(); i++) {
                    menu2.append(i + 1);
                    menu2.append(". ");
                    menu2.append(bankTransactions.get(i).getDate_added());

                    menu2.append(" | ");
                    // minus amount of transfer is stored in bank_transactions
                    if (!bankTransactions.get(i).getBank_account_id_from()
                            .equals(bankTransactions.get(i).getBank_account_id_to())
                            && bankTransactions.get(i).getBank_account_id_to().equals(bankAccountLists.get(i_account_selected - 1).getBank_account_id())
                                    ) {
                        menu2.append(formatter.format(-bankTransactions.get(i).getAmount()));
                    } else {
                        menu2.append(formatter.format(bankTransactions.get(i).getAmount()));
                    }
                    menu2.append("\n");
                }
                System.out.print(menu2);
            }

            router.navigate("/dashboard");

        } catch (InvalidRequestException | AuthenticationException e) {
            System.out.println(e.getMessage());
        }
    }
}
