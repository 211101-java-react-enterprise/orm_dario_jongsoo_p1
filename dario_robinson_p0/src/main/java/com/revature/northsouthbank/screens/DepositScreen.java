package com.revature.northsouthbank.screens;

import com.revature.northsouthbank.services.UserService;
import com.revature.northsouthbank.util.ScreenRouter;
import com.revature.northsouthbank.util.logging.Logger;

import java.io.BufferedReader;
import java.util.Date;

import static com.revature.northsouthbank.screens.ViewBalanceScreen.current_balance;

public class DepositScreen extends Screen {

    private final Logger logger;
    private final UserService bankService;
    Date currentDate = new Date();
    public DepositScreen(BufferedReader consoleReader, ScreenRouter router, UserService bankService) {
        super("DepositScreen", "/deposits", consoleReader, router);
        this.bankService = bankService;
        logger = Logger.getLogger(true);

    }

    @Override
    public void render() throws Exception {
        try {
            double depositAmount;
            logger.log(currentDate, "Attempting to deposit...");
        System.out.println("Hello! Your current balance is " + "$" + current_balance + ".");
        System.out.println(" ");
        System.out.print("How much money would you like to deposit today?\n" +
                            "Enter an amount here > ");
        depositAmount = Double.parseDouble(consoleReader.readLine());
        System.out.println("You have deposited " + "$" + depositAmount + " into your account!");
        System.out.println(" ");
        current_balance = current_balance + depositAmount;
        System.out.println("Your new balance is: " + "$" + current_balance);
        System.out.println(" ");
        logger.log(currentDate, "Deposit successful!");
        System.out.println("Taking you back to dashboard...");
        } catch (NumberFormatException n) {
            System.out.println("Invalid command. Please try again.");
            System.out.println(" ");
            logger.log(currentDate, "Deposit unsuccessful.");
            router.navigate("/deposits");
        }

        // must add a way to get balance from somewhere and add deposit amount from it
        // to create a new balance (balance after adding deposit)
    }
}
