/*
**< How to use the ORM >**

    -- Include the ORM jar in the project.
        orm_dario_jongsoo_p1.jar

        <dependency>
            <groupId>com.revature</groupId>
            <artifactId>orm-dario-jongsoo</artifactId>
            <version>1.0.0</version>
            <scope>system</scope>
            <systemPath>${project.basedir}/src/main/webapp/WEB-INF/lib/orm_dario_jongsoo_p1.jar</systemPath>
        </dependency>

    -- Database credential
        src/main/resources/db.properties
            url=jdbc:postgresql://your_hosting_server_url:port_number/postgres?currentSchema=banking
            username=postgres
            password=xxxxxxxxxx

    -- Declare a class to be made as a table in the database
        @DataSourceORM(TableName = "app_users", Schema = "banking")

    -- Column declaration
        @ColumnInORM(Constraint = "NOT NULL", Size=5, DefaultValue ="" , PRIMARY = "Y", UNIQUE = "Y", ForeignKey={"",""}, Check="")

    -- To use the ORM, initialize ORM first.
        crudORM = new CrudORM(this);

*/
package com.revature.banking.orm;

import com.revature.banking.orm.models.AppUserORM;
import com.revature.banking.orm.models.BankAccountORM;
import com.revature.banking.orm.models.Flashcard;
import com.revature.banking.orm.utils.CrudORM;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OrmMain {

    CrudORM crudORM;

    public void startORM() {
        System.out.println("ORM starting...");

        // --------------------------------------
        // To use the ORM, initialize ORM first.
        crudORM = new CrudORM(this);
        // --------------------------------------

//        testCreate();
//        testInsert();
//        testRead();
//        testReadAll();
//        testUpdate();
//        testDelete();
        testCreateAllOfTablesWithDataSourceORM();

    }

    // Create all tables of class under root package which are declared with an annotation of @DataSourceORM
    public void testCreateAllOfTablesWithDataSourceORM() {
        try {
            System.out.println(getClass().getResource("/"));

            crudORM.createAllOfTablesWithDataSourceORM(this);
        } catch (SQLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void testCreate() {
//        crudORM.createTable(AppUserORM.class);
        crudORM.createTable(BankAccountORM.class);
//        crudORM.createTable(Flashcard.class);
    }

    public void testDelete() {
        AppUserORM newUser = new AppUserORM("valid", "valid", "valid", "valid", "valid");

        Map<String, Map<String, String>> whereOrderBy = new HashMap<>();

        Map<String, String> where = new HashMap<>();
        where.put("first_name", "valid");
        whereOrderBy.put("where", where);

        AppUserORM a = new AppUserORM();
        AppUserORM appUserORM = crudORM.deletTable(newUser, whereOrderBy);
        System.out.println(appUserORM);
    }

    public void testUpdate() {
        AppUserORM newUser = new AppUserORM("valid", "valid-222", "valid", "valid", "valid");

        Map<String, Map<String, String>> whereOrderBy = new HashMap<>();

        Map<String, String> cols = new HashMap<>();
        cols.put("last_name", null);
        whereOrderBy.put("cols", cols);

        Map<String, String> where = new HashMap<>();
        where.put("first_name", "valid");
        whereOrderBy.put("where", where);

//        AppUserORM a = new AppUserORM();
        AppUserORM appUserORM = crudORM.updateTable(newUser, whereOrderBy);
        System.out.println(appUserORM);
    }

    public void testReadAll() {
        AppUserORM newUser = new AppUserORM("valid", "valid", "valid", "valid", "valid");

        Map<String, Map<String, String>> whereOrderBy = new HashMap<>();

        AppUserORM a = new AppUserORM();
        List<AppUserORM> appUserORMList = crudORM.readTable(newUser, whereOrderBy,AppUserORM.class);
        for (AppUserORM appUserORM : appUserORMList) {
            System.out.println(appUserORM);
        }
    }

    public void testRead() {
        AppUserORM newUser = new AppUserORM("valid", "valid", "valid", "valid", "valid");

        Map<String, Map<String, String>> whereOrderBy = new HashMap<>();

        Map<String, String> where = new HashMap<>();
        where.put("username", "a");
        where.put("password", "a");
        whereOrderBy.put("where", where);

        AppUserORM a = new AppUserORM();
        List<AppUserORM> appUserORMList = crudORM.readTable(newUser, whereOrderBy,AppUserORM.class);
        for (AppUserORM appUserORM : appUserORMList) {
            System.out.println(appUserORM);
        }
    }

    public void testInsert() {
//        AppUserORM testClass = new AppUserORM("valid", "valid", "valid", "valid", "valid");
//        AppUserORM testClass = new AppUserORM("valid_2", "valid_2", "valid_2", "valid_2", "valid_2");
//        testClass.setUser_id(UUID.randomUUID().toString());
//        AppUserORM rst = crudORM.insertTable(testClass);

        BankAccountORM testClass = new BankAccountORM("valid_2", "valid_2", "valid_2", 7.77, "e7fcf1cc-aef4-4326-a422-5798221d05e0");
        testClass.setBank_account_id(UUID.randomUUID().toString());
        BankAccountORM rst = crudORM.insertTable(testClass);
    }
}
