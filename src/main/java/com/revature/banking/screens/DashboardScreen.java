package com.revature.banking.screens;

import com.revature.banking.models.AppUser;
import com.revature.banking.services.UserService;
import com.revature.banking.util.ScreenRouter;

import java.io.BufferedReader;

public class DashboardScreen extends Screen {

    private final UserService userService;

    public DashboardScreen(BufferedReader consoleReader, ScreenRouter router, UserService userService) {
        super("DashboardScreen", "/dashboard", consoleReader, router);
        this.userService = userService;
    }

    @Override
    public void render() throws Exception {

        AppUser sessionUser = userService.getSessionUser();
        if (sessionUser == null) {
            System.out.println("You are not currently logged in! Navigating to Login Screen");
            router.navigate("/login");
            return;
        }

        while (userService.isSessionActive()) {
            System.out.println("\n------------------------------------------------");
            System.out.printf("%s's Dashboard\n", sessionUser.getFirstName());

            String menu =
                    "1) Open a Bank account\n" +
                    "2) Deposit funds into an account\n" +
                    "3) Withdraw funds from an account\n" +
                    "4) transfer money between accounts\n" +
                            
                    "5) View the balance of my account(s)\n" +
                    "6) view the transaction history for an account\n" +
                    "7) Log Out\n" +
                    "> ";

            System.out.print(menu);

            String userSelection = consoleReader.readLine();

            switch (userSelection) {
                case "1":
                    router.navigate("/open_bank_account");
                    break;
                case "2":
                    router.navigate("/deposit");
                    break;
                case "3":
                    router.navigate("/withdraw");
                    break;
                case "4":
                    router.navigate("/transfer");
                    break;
                case "5":
                    router.navigate("/view_balances");
                    break;
                case "6":
                    router.navigate("/view_transaction");
                    break;
                case "7":
                    userService.logout();
                    router.navigate("/welcome");
                    break;
                default:
                    System.out.println("You have made an invalid selection");
            }
        }
    }
}
