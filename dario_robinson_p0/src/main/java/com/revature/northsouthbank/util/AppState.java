package com.revature.northsouthbank.util;

import com.revature.northsouthbank.daos.AppUserDAO;
import com.revature.northsouthbank.daos.TransactionsDAO;
import com.revature.northsouthbank.screens.*;
import com.revature.northsouthbank.services.BankService;
import com.revature.northsouthbank.services.UserService;
import com.revature.northsouthbank.util.logging.Logger;
import java.util.Date;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AppState {

    private final Logger logger;
    private static boolean appRunning;
    private final ScreenRouter router;
    Date currentDate = new Date();
    public AppState() {

        logger = Logger.getLogger(true);
        logger.log(currentDate, "Initializing application...");

        appRunning = true;
        router = new ScreenRouter();
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

        AppUserDAO userDAO = new AppUserDAO();
        UserService userService = new UserService(userDAO);

        TransactionsDAO transactionsDAO = new TransactionsDAO();
        BankService services = new BankService(transactionsDAO, userService);

        router.addScreen(new WelcomeScreen(consoleReader, router));
        router.addScreen(new RegisterScreen(consoleReader, router, userService));
        router.addScreen(new LoginScreen(consoleReader, router, userService));
        router.addScreen(new DashboardScreen(consoleReader, router, userService));
        router.addScreen(new ViewBalanceScreen(consoleReader, router, userService));
        router.addScreen(new DepositScreen(consoleReader, router, userService));
        router.addScreen(new WithdrawScreen(consoleReader, router, userService));

        logger.log(currentDate, "Application initialized! Taking you to account screen...");

    }

    public void startup() {
        try {
            while (appRunning) {
                router.navigate("/welcome");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shutdown() {
        appRunning = false;
    }
}
