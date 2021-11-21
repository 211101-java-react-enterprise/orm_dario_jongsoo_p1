package com.revature.banking.models;

public class BankTransaction {

    private String bank_transaction_id;
    private String bank_account_id_from;
    private String bank_account_id_to;
    private double amount;
    private String date_added;

    private AppUser trader;
    private BankAccount bankAccount_From;
    private BankAccount bankAccount_To;

    public BankTransaction() {
    }

    public BankTransaction(double amount, AppUser trader, BankAccount bankAccountFrom) {
        this.amount = amount;
        this.trader = trader;
        this.bankAccount_From = bankAccountFrom;
    }

    public BankTransaction(String bank_transaction_id, String bank_account_id_from, double amount, AppUser trader) {
        this.bank_transaction_id = bank_transaction_id;
        this.bank_account_id_from = bank_account_id_from;
        this.amount = amount;
        this.trader = trader;
    }

    public String getBank_transaction_id() {
        return bank_transaction_id;
    }

    public void setBank_transaction_id(String bank_transaction_id) {
        this.bank_transaction_id = bank_transaction_id;
    }

    public String getBank_account_id_from() {
        return bank_account_id_from;
    }

    public void setBank_account_id_From(String bank_account_id_from) {
        this.bank_account_id_from = bank_account_id_from;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public AppUser getTrader() {
        return trader;
    }

    public void setTrader(AppUser trader) {
        this.trader = trader;
    }

    public BankAccount getBankAccount_From() {
        return bankAccount_From;
    }

    public void setBankAccount_From(BankAccount bankAccountFrom) {
        this.bankAccount_From = bankAccountFrom;
    }

    public String getBank_account_id_to() {
        return bank_account_id_to;
    }

    public void setBank_account_id_To(String bank_account_id_to) {
        this.bank_account_id_to = bank_account_id_to;
    }

    public BankAccount getBankAccount_To() {
        return bankAccount_To;
    }

    public void setBankAccount_To(BankAccount bankAccount_To) {
        this.bankAccount_To = bankAccount_To;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    @Override
    public String toString() {
        return "BankTransaction{" +
                "bank_transaction_id='" + bank_transaction_id + '\'' +
                ", bank_account_id='" + bank_account_id_from + '\'' +
                ", amount=" + amount +
                ", trader=" + trader +
                '}';
    }
}
