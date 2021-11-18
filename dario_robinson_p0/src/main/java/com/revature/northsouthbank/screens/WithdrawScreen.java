package com.revature.northsouthbank.screens;

import com.revature.northsouthbank.services.UserService;
import com.revature.northsouthbank.util.ScreenRouter;
import com.revature.northsouthbank.util.logging.Logger;

import java.io.BufferedReader;
import java.util.Date;

import static com.revature.northsouthbank.screens.ViewBalanceScreen.current_balance;

public class WithdrawScreen extends Screen {
    private final Logger logger;
    private final UserService bankService;
    Date currentDate = new Date();
    public WithdrawScreen(BufferedReader consoleReader, ScreenRouter router, UserService bankService) {

        super("WithdrawScreen", "/withdrawals", consoleReader, router);
        this.bankService = bankService;
        logger = Logger.getLogger(true);
    }

    @Override
    public void render() throws Exception {
        try {
            double withdrawAmount;
            logger.log(currentDate, "Attempting to withdraw...");
            System.out.println("Hello! Your current balance is " + "$" + current_balance + ".");
            System.out.println(" ");
            System.out.print("How much money would you like to withdraw today?\n" +
                    "Enter an amount here > ");
            withdrawAmount = Double.parseDouble(consoleReader.readLine());
            if (withdrawAmount >= 0.00 && current_balance >= withdrawAmount) {
                System.out.println("You have withdrawn " + "$" + withdrawAmount + " from your account!");
                System.out.println(" ");
                current_balance = current_balance - withdrawAmount;
                System.out.println("Your new balance is: " + "$" + current_balance);
                System.out.println(" ");
                logger.log(currentDate, "Withdrawal successful!");
                System.out.println("Taking you back to dashboard...");
            } else if (current_balance < withdrawAmount) {
                System.out.println("Cannot withdraw; insufficient funds. Please try again.");
                System.out.println(" ");
                logger.log(currentDate, "Withdrawal unsuccessful.");
                router.navigate("/withdrawals");
            }
        } catch (NumberFormatException n) {
            System.out.println("Invalid command. Please try again.");
            System.out.println(" ");
            router.navigate("/withdrawals");
        }
    }
}
