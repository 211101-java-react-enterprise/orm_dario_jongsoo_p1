package com.revature.northsouthbank.screens;

import com.revature.northsouthbank.exceptions.InvalidRequestException;
import com.revature.northsouthbank.exceptions.ResourcePersistenceException;
import com.revature.northsouthbank.models.AppUser;
import com.revature.northsouthbank.services.UserService;
import com.revature.northsouthbank.util.ScreenRouter;
import com.revature.northsouthbank.util.logging.Logger;
import java.util.Date;
import java.io.BufferedReader;

public class RegisterScreen extends Screen {

    private final Logger logger;
    private final UserService userService;
    Date currentDate = new Date();
    public RegisterScreen(BufferedReader consoleReader, ScreenRouter router, UserService userService) {
        super("RegisterScreen", "/register", consoleReader, router);
        this.userService = userService;
        logger = Logger.getLogger(true);

    }

    @Override
    public void render() throws Exception {
        System.out.println("You have selected Register.");
        System.out.println("Please provide us with some basic information.");
        System.out.print("First name: ");
        String firstName = consoleReader.readLine();

        System.out.print("Last name: ");
        String lastName = consoleReader.readLine();

        System.out.print("Email: ");
        String email = consoleReader.readLine();

        System.out.print("Username: ");
        String username = consoleReader.readLine();

        System.out.print("Password: ");
        String password = consoleReader.readLine();

        System.out.printf("Provided user first and last name: {\"firstName\": %s, \"lastName\": %s}\n", firstName, lastName);

        AppUser newUser = new AppUser(firstName, lastName, email, username, password);

        try {
            userService.registerNewUser(newUser);
            logger.log(currentDate, "Registration successful!");
            System.out.println("Registration successful!");
            router.navigate("/login");
        } catch (InvalidRequestException | ResourcePersistenceException e) {
            System.out.println(e.getMessage());

            logger.log(currentDate,"Registration unsuccessful.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
