/*

*/
public class ClientORM {

    CrudORM crudORM = new CrudORM();

    public static void main(String[] args) {

        ClientORM clientORM = new ClientORM();

        clientORM.testRead();
    }

    public void testCreateAllTablesWithDataSourceORM() {
        try {
            crudORM.testCreateAllTablesWithDataSourceORM();
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

    public void testInsert() {
        AppUserORM testClass = new AppUserORM("valid", "valid", "valid", "valid", "valid");
        //AppUserORM testClass = new AppUserORM("valid_2", "valid_2", "valid_2", "valid_2", "valid_2");
        testClass.setUser_id(UUID.randomUUID().toString());

        crudORM.insertTable(testClass);
    }
}