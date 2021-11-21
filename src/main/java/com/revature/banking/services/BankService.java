package com.revature.banking.services;

import com.revature.banking.daos.BankDAO;
import com.revature.banking.exceptions.AuthorizationException;
import com.revature.banking.exceptions.NotEnoughBalanceException;
import com.revature.banking.exceptions.ResourcePersistenceException;
import com.revature.banking.models.AppUser;
import com.revature.banking.models.BankAccount;
import com.revature.banking.models.BankTransaction;

import java.util.List;

public class BankService {

    private final BankDAO bankDAO;
    private final UserService userService;

    public BankService(BankDAO bankDAO, UserService userService) {
        this.bankDAO = bankDAO;
        this.userService = userService;
    }

    public boolean isBankAccountValid(BankAccount bankAccount) {
        if (bankAccount == null) return false;
        if (bankAccount.getAccountName() == null || bankAccount.getAccountName().trim().equals("")) return false;
        if (bankAccount.getAccountType() == null || bankAccount.getAccountType().trim().equals("")) return false;
        return true;
    }

    public boolean openBankAccount(BankAccount bankAccount) {

        if (!userService.isSessionActive()) {
            throw new AuthorizationException("No active user session to perform operation!");
        }

        bankAccount.setCreator(userService.getSessionUser());
        BankAccount registeredBankAccount = bankDAO.save(bankAccount);

        if (registeredBankAccount == null) {
            throw new ResourcePersistenceException("The bank account could not be persisted to the datasource!");
        }

        return true;
    }

    public List<BankAccount> getBankAccountsByUserId() {
        return getBankAccountsByUserId("Me");
    }

    public List<BankAccount> getBankAccountsByUserId(String Others_or_Me) {

        if (!userService.isSessionActive()) {
            throw new AuthorizationException("No active user session to perform operation!");
        }

        AppUser sessionUser = userService.getSessionUser();
        String userId = sessionUser.getId();

        List<BankAccount> registeredBankAccounts = bankDAO.findBankAccountsByUserId(userId, Others_or_Me);

        if (registeredBankAccounts == null) {
            throw new ResourcePersistenceException("The bank account could not be persisted to the datasource!");
        }

        return registeredBankAccounts;
    }


    public List<BankAccount> getBankAccountsByOthersThanBankAccountId(String bankAccount_from_this) {

        if (!userService.isSessionActive()) {
            throw new AuthorizationException("No active user session to perform operation!");
        }

        AppUser sessionUser = userService.getSessionUser();
        String userId = sessionUser.getId();

        List<BankAccount> registeredBankAccounts = bankDAO.findBankAccountsByOthersThanBankAccountId(bankAccount_from_this);

        if (registeredBankAccounts == null) {
            throw new ResourcePersistenceException("The bank account could not be persisted to the datasource!");
        }

        return registeredBankAccounts;
    }

    public BankTransaction transact(BankTransaction bankTransaction) {

        if (!userService.isSessionActive()) {
            throw new AuthorizationException("No active user session to perform operation!");
        }

        bankTransaction.setTrader(userService.getSessionUser());
        // withdraw, transfer
        if (bankTransaction.getBankAccount_From().getBalance() + bankTransaction.getAmount() < 0) {
            throw new NotEnoughBalanceException();
        }

        bankTransaction = bankDAO.transact(bankTransaction);

        if (bankTransaction == null) {
            throw new ResourcePersistenceException("The transaction could not be persisted to the datasource!");
        }

        return bankTransaction;
    }

    public List<BankTransaction> getTransactionsByUserAccountId(BankAccount bankAccount) {

        if (!userService.isSessionActive()) {
            throw new AuthorizationException("No active user session to perform operation!");
        }

        List<BankTransaction> bankTransactions = bankDAO.findTransactionsByUserAccountId(bankAccount.getBank_account_id());

        if (bankTransactions == null) {
            throw new ResourcePersistenceException("The transactions could not be persisted to the datasource!");
        }

        return bankTransactions;
    }

    public double deposit(BankAccount bankAccount, double amount) {

        return 0;
    }

    public double withdraw(BankAccount bankAccount, double amount) {

        return 0;
    }

    public BankAccount[] viewBalances() {

        return null;
    }
}
