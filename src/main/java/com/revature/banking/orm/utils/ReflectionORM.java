package com.revature.banking.orm.utils;


import com.revature.banking.orm.annotation.ColumnInORM;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ReflectionORM {

    public static List<Field> getFieldNames(Class<?> aClass) {
        List<Field> fields = new LinkedList<>();
        for (Field field : aClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(ColumnInORM.class)) {
                fields.add(field);
            }
        }
        return fields;
    }

    public static <T> List<String> getClassNamesInPackage(Class<T> aClass, String packageName) throws URISyntaxException {
        if (Objects.requireNonNull(aClass.getResource("/")).toURI().toString().contains("WEB-INF")) {
            return getClassNamesInPackage_inner(2, aClass, "/", packageName);
        } else {
            return getClassNamesInPackage_inner(1, aClass, "target/classes/", packageName);
        }
    }

    public static <T> List<String> getClassNamesInPackage_inner(int cate, Class<T> aClass, String directory_root, String packageName)
            throws URISyntaxException {

        List<String> classNames = new ArrayList<>();
        File packageDir;
        String directory_package = directory_root + packageName.replace('.', '/');
        if (cate == 1) {
            packageDir = new File(directory_package);
        } else {
            packageDir = new File(Objects.requireNonNull(aClass.getResource(directory_package)).toURI());
        }

        File[] packageFiles = packageDir.listFiles();

        StringBuilder pkgBuilder = new StringBuilder(packageName);
        if (packageFiles != null) {
            for (File file : packageFiles) {
                if (file.isDirectory()) {
                    pkgBuilder.append(".").append(file.getName());
                    classNames.addAll(getClassNamesInPackage_inner(cate, aClass, directory_root, packageName + "." + file.getName()));
                    pkgBuilder = new StringBuilder(packageName);
                } else if (file.getName().endsWith(".class")) {
                    String className = pkgBuilder + "." + file.getName().substring(0, file.getName().length() - 6);
                    classNames.add(className);
                }
            }
        }
        return classNames;
    }
}
