public class ClientOrmMain {

    CrudORM crudORM;

    public static void main(String[] args) {
        ClientOrmMain clientOrmMain = new ClientOrmMain();
        clientOrmMain.startORM();
    }

    public void startORM() {
        System.out.println("ORM starting...");

        // --------------------------------------
        // include the ORM jar in your project.
        // To use the ORM, initialize ORM first.
        crudORM = new CrudORM(this);
        // --------------------------------------

//        testCreate();
//        testInsert();
//        testRead();
//        testUpdate();
//        testDelete();
testCreateAllOfTablesWithDataSourceORM();

    }
/*
-- database credential
src/main/resources/db.properties
url=jdbc:postgresql://your_hosting_server_url:port_number/postgres?currentSchema=banking
username=postgres
password=xxxxxxxxxx

    -- declare a class as a target to be made as a table in the database
    @DataSourceORM(TableName = "app_users", Schema = "banking")

    -- column declaration
    @ColumnInORM(Constraint = "NOT NULL", Size=5, DefaultValue ="" , PRIMARY = "Y", UNIQUE = "Y", ForeignKey={"",""}, Check="")

    -- To use the ORM, initialize ORM first.
    crudORM = new CrudORM(this);

*/

    // Create all tables of class under root package which are declared with an annotation of @DataSourceORM
    public void testCreateAllOfTablesWithDataSourceORM() {
        try {
            crudORM.testCreateAllOfTablesWithDataSourceORM();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void testCreate() {
        //CrudORM.createTable(AppUserORM.class);
        crudORM.createTable(BankAccountORM.class);
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

    public void testUpdate() {
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
        where.put("first_name", "valid");
        whereOderBy.put("where", where);

        AppUserORM a = new AppUserORM();
        List<AppUserORM> appUserORMList = crudORM.readTable(newUser, whereOderBy, AppUserORM.class);
        for (AppUserORM appUserORM : appUserORMList) {
            System.out.println(appUserORM);
        }
    }

    public void testInsert() {
        //AppUserORM testClass = new AppUserORM("valid", "valid", "valid", "valid", "valid");
        //AppUserORM testClass = new AppUserORM("valid_2", "valid_2", "valid_2", "valid_2", "valid_2");
        //testClass.setUser_id(UUID.randomUUID().toString());
        BankAccountORM testClass = new BankAccountORM("valid_2", "valid_2", "valid_2", 7.77, "valid_2");
        testClass.setBank_account_id(UUID.randomUUID().toString());

        crudORM.insertTable(testClass);
    }
}
