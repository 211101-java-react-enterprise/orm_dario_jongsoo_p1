package com.revature.northsouthbank.screens;

import com.revature.northsouthbank.util.AppState;
import com.revature.northsouthbank.util.ScreenRouter;

import java.io.BufferedReader;

public class WelcomeScreen extends Screen {

    public WelcomeScreen(BufferedReader consoleReader, ScreenRouter router) {
        super("WelcomeScreen", "/welcome", consoleReader, router);
    }

    @Override
    public void render() throws Exception {

        System.out.print("\nWelcome to NorthSouth! What can we do for you today?\n" +
                "1) Register\n" +
                "2) Login\n" +
                "3) Exit\n" +
                "Enter choice here > ");

        String userSelection = consoleReader.readLine();

        switch (userSelection) {
            case "1":
                router.navigate("/register");
                break;
            case "2":
                router.navigate("/login");
                break;
            case "3":
                System.out.println("Exiting...");
                AppState.shutdown();
                break;
            case "throw exception":
                throw new RuntimeException();
            default:
                System.out.println("You have made an invalid selection, please try again!");
        }
    }
}

