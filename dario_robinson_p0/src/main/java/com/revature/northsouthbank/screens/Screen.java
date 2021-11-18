package com.revature.northsouthbank.screens;

import com.revature.northsouthbank.util.ScreenRouter;
import com.revature.northsouthbank.util.logging.Logger;

import java.io.BufferedReader;


public abstract class Screen {

    protected Logger logger = Logger.getLogger(true);
    protected String name;
    protected String route;
    protected BufferedReader consoleReader;
    protected ScreenRouter router;

    public Screen (String name, String route, BufferedReader consoleReader, ScreenRouter router) {

        this.name = name;
        this.route = route;
        this.consoleReader = consoleReader;
        this.router = router;
    }

    public final String getName() { return name; }

    public final String getRoute() { return route; }

    public abstract void render() throws Exception;

}
