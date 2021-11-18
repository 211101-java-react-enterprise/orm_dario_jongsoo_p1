package com.revature.northsouthbank.models;

public class Transactions {

    private String id;
    private double currentBalance;
    private double deposits;
    private double withdrawals;
    private AppUser accountHolder;


    public Transactions(double currentBalance, double deposits, double withdrawals) {
        this.currentBalance = currentBalance;
        this.deposits = deposits;
        this.withdrawals = withdrawals;
    }

    public Transactions(double currentBalance, double deposits, double withdrawals, AppUser accountHolder) {
        this(currentBalance, deposits, withdrawals);
        this.accountHolder = accountHolder;
    }

    public Transactions(String id, double currentBalance, double deposits, double withdrawals, AppUser accountHolder) {
        this(currentBalance, deposits, withdrawals, accountHolder);
        this.id = id;
    }

    public Transactions() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public double getDeposits() {
        return deposits;
    }

    public void setDeposits(double deposits) {
        this.deposits = deposits;
    }

    public double getWithdrawals() {
        return withdrawals;
    }

    public void setWithdrawals(double withdrawls) {
        this.withdrawals = withdrawals;
    }

    public AppUser getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(AppUser accountHolder) {
        this.accountHolder = accountHolder;
    }
}
