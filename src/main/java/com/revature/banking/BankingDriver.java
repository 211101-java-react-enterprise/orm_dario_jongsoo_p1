package com.revature.banking;

import com.revature.banking.orm.OrmMain;

public class BankingDriver {

    public static void main(String[] args) {
        OrmMain ormMain = new OrmMain();
        ormMain.startORM();
    }
}
