package com.revature.banking.orm.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class InitORM {

    public static String packageNameInOrm;

    public static String dbUrl;
    public static String dbUsername;
    public static String dbPassword;

    public InitORM(Object newData) {
        initORMMethod(newData);
    }

    public static String getPackageNameInOrm() {
        return packageNameInOrm;
    }

    public static void setPackageNameInOrm(String packageNameInOrm) {
        InitORM.packageNameInOrm = packageNameInOrm;
    }

    public static String getDbUrl() {
        return dbUrl;
    }

    public static void setDbUrl(String dbUrl) {
        InitORM.dbUrl = dbUrl;
    }

    public static String getDbUsername() {
        return dbUsername;
    }

    public static void setDbUsername(String dbUsername) {
        InitORM.dbUsername = dbUsername;
    }

    public static String getDbPassword() {
        return dbPassword;
    }

    public static void setDbPassword(String dbPassword) {
        InitORM.dbPassword = dbPassword;
    }

    // --- to be started before using the ORM
    public void initORMMethod(Object newData) {
        // this class package name
        String packageNameInMETA = paakageInfoInMETA();
        System.out.println("packageNameInMETA Name = " + packageNameInMETA);

        String packageNameInThisClass = newData.getClass().getPackage().getName();
        System.out.println("Package Name = " + packageNameInThisClass);

        String[] pns;
        if (packageNameInMETA == null) {
            pns = packageNameInThisClass.split("\\.");
        } else {
            pns = packageNameInMETA.split("\\.");
        }

        packageNameInOrm = pns[0];
    }

    public String paakageInfoInMETA() {

        try (InputStream inputStream = getClass().getResourceAsStream("/META-INF/MANIFEST.MF")) {
            Properties props = new Properties();
            assert inputStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                props.load(reader);
                return props.getProperty("Main-Class");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
