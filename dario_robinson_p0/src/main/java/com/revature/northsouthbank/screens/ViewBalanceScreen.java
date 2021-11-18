package com.revature.northsouthbank.screens;

import com.revature.northsouthbank.services.UserService;
import com.revature.northsouthbank.util.ScreenRouter;
import com.revature.northsouthbank.util.logging.Logger;

import java.io.BufferedReader;
import java.util.Date;

public class ViewBalanceScreen extends Screen {

    public static double current_balance = 0;
    private final UserService userService;
    private final Logger logger;
    Date currentDate = new Date();
    public ViewBalanceScreen(BufferedReader consoleReader, ScreenRouter router, UserService userService) {
        super("ViewBalanceScreen", "/balance", consoleReader, router);
        this.userService = userService;
        logger = Logger.getLogger(true);
    }

    @Override
    public void render() throws Exception {
        logger.log(currentDate, "Attempting to View Balance...");
        System.out.println("Hello! Your current balance is " + "$" + current_balance);
        System.out.println(" ");
        System.out.println("What would you like to do now?");

        String balanceMenu = "1) Deposit money\n" +
                "2) Withdraw money\n" +
                "3) Return to Dashboard\n" +
                "Enter choice here > ";

        System.out.print(balanceMenu);

        logger.log(currentDate, "Check Balance successful!");
        String balanceSelection = consoleReader.readLine();

        switch (balanceSelection) {
            case "1":
                System.out.println("Ok! Taking you to Deposit screen...");
                router.navigate("/deposits");
                break;
            case "2":
                System.out.println("Ok! Taking you to Withdrawal screen...");
                router.navigate("/withdrawals");
                break;
            case "3":
                System.out.println("Ok! Taking you to Dashboard...");
                router.navigate("/dashboard");
                break;
            default:
                System.out.println("Invalid selection. Please enter 1, 2, or 3.");
                router.navigate("/balance");
        }
    }
}
