package com.revature.northsouthbank.util.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

        private static final String ANSI_RESET = "\u001B[0m";
        private static final String ANSI_YELLOW = "\u001B[33m";
        private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");


        private static Logger logger;
        private final boolean printToConsole;
        private static final Date currentDate = new Date();
        private Logger(boolean printToConsole) {

            this.printToConsole = printToConsole;
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        public static Logger getLogger(boolean printToConsole) {
            if (logger == null) {
                logger = new Logger(printToConsole);
            }

            return logger;
        }

        public void info(String message) {

        }

        public void warn(String message) {

        }

        public void error(String message) {

        }

        public void fatal(String message) {

        }

        public void log(Date currentDate, String message, Object... args) {

            try (Writer logWriter = new FileWriter("src/main/resources/logs/app.log", true)) {

                String formattedMsg = String.format(message, currentDate);
                logWriter.write(currentDate  + " " + formattedMsg + "\n");

                if (printToConsole) {
                   // System.out.println(currentDate + " " + ANSI_YELLOW + formattedMsg + ANSI_RESET);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
