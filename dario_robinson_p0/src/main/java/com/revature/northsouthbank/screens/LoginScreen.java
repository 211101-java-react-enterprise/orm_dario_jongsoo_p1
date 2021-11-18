package com.revature.northsouthbank.screens;

import com.revature.northsouthbank.exceptions.AuthenticationException;
import com.revature.northsouthbank.exceptions.InvalidRequestException;
import com.revature.northsouthbank.services.UserService;
import com.revature.northsouthbank.util.ScreenRouter;
import com.revature.northsouthbank.util.logging.Logger;

import java.io.BufferedReader;
import java.util.Date;

public class LoginScreen extends Screen {

    private final UserService userService;
    private final Logger logger;
    Date currentDate = new Date();
    public LoginScreen(BufferedReader consoleReader, ScreenRouter router, UserService userService) {
        super("LoginScreen", "/login", consoleReader, router);
        this.userService = userService;
        logger = Logger.getLogger(true);
    }

    @Override
    public void render() throws Exception {
        logger.log(currentDate, "Attempting to login...");
        System.out.println("\nPlease provide your username and password to log into your account.");
        System.out.print("Username > ");
        String username = consoleReader.readLine();
        System.out.print("Password > ");
        String password = consoleReader.readLine();

        try {
            userService.authenticateUser(username, password);
            logger.log(currentDate, "Login Successful!");
            router.navigate("/dashboard");
        } catch (InvalidRequestException | AuthenticationException e) {
            logger.log(currentDate, "Login failed.");
            System.out.println(e.getMessage());
        }
    }
}
