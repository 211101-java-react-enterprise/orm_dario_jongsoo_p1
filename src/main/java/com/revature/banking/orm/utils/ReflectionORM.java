package com.revature.banking.orm.utils;


import com.revature.banking.orm.annotation.ColumnInORM;
import com.revature.banking.orm.annotation.DataSourceORM;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectionORM {

    public static List<Field> getFieldNamesAndValues(Class<?> aClass) {

        List<Field> fields = new LinkedList<>();

        for (Field field : aClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(ColumnInORM.class)) {
                fields.add(field);
            }
        }
        return fields;
    }

    public static void getClassesInPackage() {

        //List<String> listClassesNames = getClassNamesInPackage("com.revature");
        List<String> listClassesNames = getClassNamesInPackage(InitORM.packageNameInOrm);
        List<Class<?>> classList = listClassesNames.stream()
                .map(ClassNameToClassMapper.getInstance())
                .filter(clazz -> clazz.isAnnotationPresent(DataSourceORM.class))
                .collect(Collectors.toList());

        System.out.println("+---------------------------------------------------------------------------------------+");
        System.out.printf("Found <<--- %d --->> target classes.\n", classList.size());

        System.out.println("+---------------------------------------------------------------------------------------+");
        for (Class<?> aClass : classList) {
            System.out.println("Running for: " + aClass.getName() + "\n");

            System.out.println("+---------------------------------------------------------------------------------------+");
            DataSourceORM tableAnnotation = aClass.getAnnotation(DataSourceORM.class);
            System.out.printf("Table Name <<--- %s --->>\n", tableAnnotation.TableName());

            List<Field> fields = getFieldNamesAndValues(aClass);
            System.out.println("+---------------------------------------------------------------------------------------+");
            System.out.print("Column Names :\n");
            for (Field f : fields) {
                System.out.println(f.getName());
            }
            //selectTable(aClass,fields );
        }
    }

    public static List<String> getClassNamesInPackage(String packageName) {

        List<String> classNames = new ArrayList<>();
        File packageDir = new File("target/classes/" + packageName.replace('.', '/'));
        File[] packageFiles = packageDir.listFiles();

        StringBuilder pkgBuilder = new StringBuilder(packageName);
        if (packageFiles != null) {
            for (File file : packageFiles) {
                if (file.isDirectory()) {
                    pkgBuilder.append(".").append(file.getName());
                    classNames.addAll(getClassNamesInPackage(packageName + "." + file.getName()));
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
