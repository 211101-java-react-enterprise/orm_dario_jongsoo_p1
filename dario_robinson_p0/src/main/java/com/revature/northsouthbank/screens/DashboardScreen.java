package com.revature.northsouthbank.screens;

import com.revature.northsouthbank.models.AppUser;
import com.revature.northsouthbank.services.UserService;
import com.revature.northsouthbank.util.ScreenRouter;

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
            System.out.println("You are not logged in! Navigating back to Login Screen...");
            router.navigate("login");
            return;
        }

        while (userService.isSessionActive()) {
            System.out.printf("\n%s's Dashboard\n", sessionUser.getFirstName());

            String menu = "1) View Account Balance\n" +
                    "2) Deposit money\n" +
                    "3) Withdraw money\n" +
                    "4) Logout\n" +
                    "Enter choice here > ";

            System.out.print(menu);

            String userSelection = consoleReader.readLine();

            switch (userSelection) {
                case "1":
                    router.navigate("/balance");
                    break;
                case "2":
                    router.navigate("/deposits");
                    break;
                case "3":
                    router.navigate("/withdrawals");
                    break;
                case "4":
                    userService.logout();
                    break;
                default:
                    System.out.println("You have made an invalid selection.");
            }
        }
    }
}
