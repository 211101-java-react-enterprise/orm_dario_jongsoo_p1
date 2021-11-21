package com.revature.banking.screens.bank;

import com.revature.banking.services.BankService;
import com.revature.banking.util.ScreenRouter;

import java.io.BufferedReader;

public class WithdrawScreen extends TransactionScreen {
    public WithdrawScreen(BufferedReader consoleReader, ScreenRouter router, BankService bankService) {
        super(consoleReader, router, bankService,"withdraw");
    }
}
