package com.revature.northsouthbank.services;

import com.revature.northsouthbank.daos.TransactionsDAO;
import com.revature.northsouthbank.exceptions.AuthorizationException;
import com.revature.northsouthbank.models.Transactions;
import com.revature.northsouthbank.util.collections.List;

public class BankService {

    private final TransactionsDAO transactionDAO;
    private final UserService userService;

    public BankService(TransactionsDAO transactionDAO, UserService userService) {
        this.transactionDAO = transactionDAO;
        this.userService = userService;
    }

    public List<Transactions> listAllTransactions() {
        return transactionDAO.findAll();
    }

    public List<Transactions> findMyTransactions() {

        if(!userService.isSessionActive()) {
            throw new AuthorizationException("No active user session to perform operation!");
        }

        return transactionDAO.findAccountsByAccountHolderId(userService.getSessionUser().getId());
    }

}
