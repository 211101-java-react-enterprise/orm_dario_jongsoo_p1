package com.revature.banking.util;

import com.revature.banking.daos.AppUserDAO;
import com.revature.banking.daos.BankDAO;
import com.revature.banking.orm.OrmMain;
import com.revature.banking.screens.DashboardScreen;
import com.revature.banking.screens.LoginScreen;
import com.revature.banking.screens.RegisterScreen;
import com.revature.banking.screens.WelcomeScreen;
import com.revature.banking.screens.bank.*;
import com.revature.banking.services.BankService;
import com.revature.banking.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/*
    Encapsulate of application state. We will create a few things in here that will be used throughout the
    application:
        - a common BufferedReader that all screens can use to read from the console
        - a ScreenRouter that can be used to navigate from one screen to another
        - a boolean that indicates if the app is still running or not
 */
public class AppState {

    protected Logger logger = LogManager.getLogger();
    private static boolean appRunning;
    private final ScreenRouter router;

    public AppState() {

        final Logger logger = LogManager.getLogger();
        logger.info("Initializing application...");

        appRunning = true;
        router = new ScreenRouter();
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

        AppUserDAO userDAO = new AppUserDAO();
        UserService userService = new UserService(userDAO);

        //userService.authenticateUser("asd", "asd"); // test - to avoid login

        BankDAO bankDAO = new BankDAO();
        BankService bankService = new BankService(bankDAO, userService);

        router.addScreen(new WelcomeScreen(consoleReader, router));
        router.addScreen(new RegisterScreen(consoleReader, router, userService));
        router.addScreen(new LoginScreen(consoleReader, router, userService));
        router.addScreen(new DashboardScreen(consoleReader, router, userService));

        router.addScreen(new OpenAccountScreen(consoleReader, router, bankService));
        router.addScreen(new DepositScreen(consoleReader, router, bankService));
        router.addScreen(new WithdrawScreen(consoleReader, router, bankService));
        router.addScreen(new TransferScreen(consoleReader, router, bankService));
        router.addScreen(new ViewBalancesScreen(consoleReader, router, bankService));
        router.addScreen(new ViewTransactionScreen(consoleReader, router, bankService));

        logger.info("Successful application initialized!");
    }

    public void startOrm() {

        try {
            OrmMain ormMain = new OrmMain();
            ormMain.startOrm();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startup() {

        try {
            while (appRunning) {
                router.navigate("/welcome");
                //router.navigate("/dashboard"); // test
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shutdown() {
        appRunning = false;
    }
}
