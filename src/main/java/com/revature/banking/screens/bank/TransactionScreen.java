package com.revature.banking.screens.bank;

import com.revature.banking.exceptions.AuthenticationException;
import com.revature.banking.exceptions.InvalidRequestException;
import com.revature.banking.exceptions.NotEnoughBalanceException;
import com.revature.banking.models.BankAccount;
import com.revature.banking.models.BankTransaction;
import com.revature.banking.screens.Screen;
import com.revature.banking.services.BankService;
import com.revature.banking.util.Misc;
import com.revature.banking.util.ScreenRouter;

import java.io.BufferedReader;
import java.util.LinkedList;
import java.util.List;

public class TransactionScreen extends Screen {

    private final BankService bankService;
    private String cate_of_transaction;
    private int i_account_selected_from;
    private int i_account_selected_to;
    private String bankAccount_from_this;

    public TransactionScreen(BufferedReader consoleReader, ScreenRouter router, BankService bankService, String cate_of_transaction) {
        super("LoginScreen", "/" + cate_of_transaction, consoleReader, router);
        this.bankService = bankService;
        this.cate_of_transaction = cate_of_transaction;
    }

    public String getCate_of_transaction() {
        return cate_of_transaction;
    }

    public void setCate_of_transaction(String cate_of_transaction) {
        this.cate_of_transaction = cate_of_transaction;
    }

    private List<BankAccount> render_accounts(List<BankAccount> bankAccountLists, String From_or_To) throws Exception {
        StringBuilder menu =
                new StringBuilder("\nPlease provide which bank account you want to " + cate_of_transaction + ".\n");

        if (From_or_To.equals("From")) {
            bankAccountLists = bankService.getBankAccountsByUserId("Me");
        } else if (From_or_To.equals("To")) {
            bankAccountLists = bankService.getBankAccountsByUserId("Other");
        } else if (From_or_To.equals("Not_This_Account")) {
            bankAccountLists = bankService.getBankAccountsByOthersThanBankAccountId(bankAccount_from_this);
        }

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

        String account_selected;
        int i_account_selected = 0;
        do {
            System.out.print(menu);
            account_selected = consoleReader.readLine();
            if (!Misc.isNumeric(account_selected)) {
                System.out.println("You have made an invalid selection");
                continue;
            }
            try {
                i_account_selected = Integer.parseInt(account_selected);
            }catch (NumberFormatException e){
                System.out.println("You have made an invalid selection");
                continue;
            }
            if (i_account_selected == bankAccountLists.size() + 1) {
                router.navigate("/dashboard");
            } else if (0 < i_account_selected && i_account_selected <= bankAccountLists.size()) {
                break;
            } else {
                System.out.println("You have made an invalid selection");
            }
        }
        while (true);
        if (From_or_To.equals("To") || From_or_To.equals("Not_This_Account")) {
            i_account_selected_to = i_account_selected;
        } else {
            i_account_selected_from = i_account_selected;
        }
        return bankAccountLists;
    }

    @Override
    public void render() throws Exception {

        try {
            List<BankAccount> bankAccountLists_from = new LinkedList<>();
            List<BankAccount> bankAccountLists_to = new LinkedList<>();
            // bankAccount_From
            bankAccountLists_from = render_accounts(bankAccountLists_from, "From");
            bankAccount_from_this = bankAccountLists_from.get(i_account_selected_from - 1).getBank_account_id();
            // bankAccount_To
            if (cate_of_transaction.equals("transfer")) {
                bankAccountLists_to = render_accounts(bankAccountLists_to, "Not_This_Account");
            } else {
                bankAccountLists_to = bankAccountLists_from;
                i_account_selected_to = i_account_selected_from;
            }

            // amount
            System.out.print("\nHow much do you want to " + getCate_of_transaction() + "?\n" + "> ");
            String amountInput = consoleReader.readLine();
            if (!Misc.isNumeric(amountInput)) {
                System.out.println("You have input an invalid number");
                router.navigate("/dashboard");
                return;
            }
            double amount = Double.parseDouble(amountInput);
            if(amount <= 0){
                System.out.println("You have input an invalid number");
                router.navigate("/dashboard");
                return;
            }
            if (cate_of_transaction.equals("withdraw") || cate_of_transaction.equals("transfer")) {
                amount = -amount;
            }

            BankTransaction bankTransaction = new BankTransaction();
            bankTransaction.setAmount(amount);

            // From
            bankTransaction.setBank_account_id_From(bankAccountLists_from.get(i_account_selected_from - 1).getBank_account_id());
            bankTransaction.setBankAccount_From(bankAccountLists_from.get(i_account_selected_from - 1));
            // To
            bankTransaction.setBank_account_id_To(bankAccountLists_to.get(i_account_selected_to - 1).getBank_account_id());
            bankTransaction.setBankAccount_To(bankAccountLists_to.get(i_account_selected_to - 1));

            try {
                bankTransaction = bankService.transact(bankTransaction);
                logger.info("Successful transacted!");
                router.navigate("/dashboard");
            } catch (NotEnoughBalanceException e) {
                System.out.println("------------------------------------------------\n");
                System.out.println("Not enough balance");
                System.out.println("------------------------------------------------\n");
                router.navigate("/dashboard");
            } catch (InvalidRequestException | AuthenticationException e) {
                System.out.println(e.getMessage());
            }
        } catch (InvalidRequestException | AuthenticationException e) {
            System.out.println(e.getMessage());
        }
    }
}
