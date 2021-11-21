package com.revature.banking.orm;

import com.revature.banking.orm.models.AppUserORM;
import com.revature.banking.orm.models.BankAccountORM;
import com.revature.banking.orm.utils.CrudORM;
import com.revature.banking.orm.utils.ReflectionORM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OrmMain {

    CrudORM crudORM = new CrudORM();

    public void startOrm() {
        System.out.println("fdsaljdas");
        //ReflectionORM.getClassesInPackage();

        //testCreate();
        //testInsert();
        //testRead();
        //testUdate();
        testDelete();

    }

    public void testDelete() {
        AppUserORM newUser = new AppUserORM("valid", "valid", "valid", "valid", "valid");

        Map<String, Map<String, String>> whereOderBy = new HashMap<>();

        Map<String, String> where = new HashMap<>();
        where.put("first_name", "valid");
        whereOderBy.put("where", where);

        AppUserORM a = new AppUserORM();
        AppUserORM appUserORM = crudORM.deletTable(newUser, whereOderBy);
        System.out.println(appUserORM);
    }

    public void testUdate() {
        AppUserORM newUser = new AppUserORM("valid", "valid", "valid", "valid", "valid");

        Map<String, Map<String, String>> whereOderBy = new HashMap<>();

        Map<String, String> cols = new HashMap<>();
        cols.put("last_name", "valid-update");
        whereOderBy.put("cols", cols);

        Map<String, String> where = new HashMap<>();
        where.put("first_name", "valid");
        whereOderBy.put("where", where);

        AppUserORM a = new AppUserORM();
        AppUserORM appUserORM = crudORM.updateTable(newUser, whereOderBy);
        System.out.println(appUserORM);
    }

    public void testRead() {
        AppUserORM newUser = new AppUserORM("valid", "valid", "valid", "valid", "valid");

        Map<String, Map<String, String>> whereOderBy = new HashMap<>();

        Map<String, String> where = new HashMap<>();
        where.put("first_name", "valid_2");
        whereOderBy.put("where", where);

        AppUserORM a = new AppUserORM();
        List<AppUserORM> appUserORMList = crudORM.readTable(newUser, whereOderBy, AppUserORM.class);
        for (AppUserORM appUserORM : appUserORMList) {
            System.out.println(appUserORM);
        }
    }

    public void testCreate() {
        //CrudORM.createTable(AppUserORM.class);
        crudORM.createTable(BankAccountORM.class);
    }

    public void testInsert() {
        AppUserORM testClass = new AppUserORM("valid", "valid", "valid", "valid", "valid");
        //AppUserORM testClass = new AppUserORM("valid_2", "valid_2", "valid_2", "valid_2", "valid_2");
        testClass.setUser_id(UUID.randomUUID().toString());
//        BankAccountORM testClass = new BankAccountORM("valid_2", "valid_2", "valid_2", 7.77, "valid_2");
//        testClass.setBank_account_id(UUID.randomUUID().toString());

        crudORM.insertTable(testClass);
    }
}
