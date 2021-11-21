package com.revature.banking;

import com.revature.banking.util.AppState;

public class BankingDriver {

    public static void main(String[] args) {
        AppState app = new AppState();
        //app.startup();
        app.startOrm();
    }
}
