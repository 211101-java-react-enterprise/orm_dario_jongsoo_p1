package com.revature.northsouthbank.daos;

import com.revature.northsouthbank.models.AppUser;
import com.revature.northsouthbank.models.Transactions;
import com.revature.northsouthbank.util.collections.LinkedList;
import com.revature.northsouthbank.util.collections.List;
import com.revature.northsouthbank.util.datasource.ConnectionFactory;

import java.sql.*;
import java.util.UUID;

public class TransactionsDAO implements CrudDAO<Transactions> {

    public List<Transactions> findAccountsByAccountHolderId(String accountHolderId) {

        List<Transactions> transactions = new LinkedList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            String sql = "select * " +
                        "from account_transactions at2 " +
                        "join bank_accounts ba " +
                        "on at2.account_holder_id = ba.user_id " +
                        "where ba.user_id = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, accountHolderId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Transactions userTransactions= new Transactions();
                AppUser accountHolder = new AppUser();
                userTransactions.setId(rs.getString("account_id"));
                userTransactions.setCurrentBalance(rs.getDouble("current_balance"));
                userTransactions.setDeposits(rs.getDouble("deposits"));
                userTransactions.setWithdrawals(rs.getDouble("withdrawals"));
                accountHolder.setId(rs.getString("user_id"));
                accountHolder.setFirstName(rs.getString("first_name"));
                accountHolder.setLastName(rs.getString("last_name"));
                accountHolder.setEmail(rs.getString("email"));
                accountHolder.setUsername(rs.getString("username"));
                userTransactions.setAccountHolder(accountHolder);
                transactions.add(userTransactions);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    @Override
    public Transactions save(Transactions newAccount) {

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            newAccount.setId(UUID.randomUUID().toString());

            String sql = "insert into account_transactions (account_id, current_balance, deposits, withdrawals, account_holder_id) values (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newAccount.getId());
            pstmt.setDouble(2, newAccount.getCurrentBalance());
            pstmt.setDouble(3, newAccount.getDeposits());
            pstmt.setDouble(4, newAccount.getWithdrawals());
            pstmt.setString(5, newAccount.getAccountHolder().getId());

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted != 0) {
                return newAccount;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Transactions> findAll() {

        List<Transactions> transactions = new LinkedList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            String sql = "select * from account_transactions at2 join bank_accounts ba on at2.account_holder_id = u.id";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Transactions transaction = new Transactions();
                AppUser accountHolder = new AppUser();
                transaction.setId(rs.getString("account_id"));
                transaction.setCurrentBalance(rs.getDouble("current_balance"));
                transaction.setDeposits(rs.getDouble("deposits"));
                transaction.setWithdrawals(rs.getDouble("withdrawals"));
                accountHolder.setId(rs.getString("user_id"));
                accountHolder.setFirstName(rs.getString("first_name"));
                accountHolder.setLastName(rs.getString("last_name"));
                accountHolder.setEmail(rs.getString("email"));
                accountHolder.setUsername(rs.getString("username"));
                transaction.setAccountHolder(accountHolder);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    @Override
    public Transactions findByID(String id) {
        return null;
    }

    @Override
    public boolean update(Transactions updatedObj) {
        return false;
    }

    @Override
    public boolean removeById(String id) {
        return false;
    }
}
