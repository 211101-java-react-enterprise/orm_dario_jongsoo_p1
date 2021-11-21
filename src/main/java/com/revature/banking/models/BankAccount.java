package com.revature.banking.models;

public class BankAccount {

    private String bank_account_id;
    private String accountName;
    private String accountNumber;
    private boolean isJoinedAccount;
    private String accountType;
    private double balance;
    private AppUser creator;
    private String date_added;

    public BankAccount() {
    }

    public BankAccount(String accountName, String accountType) {
        this.accountName = accountName;
        this.accountType = accountType;
    }

    public BankAccount(String accountName, String accountNumber, boolean isJoinedAccount, String accountType, double balance) {
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.isJoinedAccount = isJoinedAccount;
        this.accountType = accountType;
        this.balance = balance;
    }

    public BankAccount(String bank_account_id, String accountName, String accountNumber, boolean isJoinedAccount,
                       String accountType, double balance, AppUser creator) {
        this(accountName, accountNumber, isJoinedAccount, accountType, balance);
        this.bank_account_id = bank_account_id;
        this.creator = creator;
    }

    public String getBank_account_id() {
        return bank_account_id;
    }

    public void setBank_account_id(String bank_account_id) {
        this.bank_account_id = bank_account_id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public boolean isJoinedAccount() {
        return isJoinedAccount;
    }

    public void setJoinedAccount(boolean joinedAccount) {
        isJoinedAccount = joinedAccount;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public AppUser getCreator() {
        return creator;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setCreator(AppUser creator) {
        this.creator = creator;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "id='" + bank_account_id + '\'' +
                ", accountName='" + accountName + '\'' +
                ", AccountNumber='" + accountNumber + '\'' +
                ", isJoinedAccount=" + isJoinedAccount +
                ", accountType=" + accountType +
                '}';
    }
}
