package com.revature.northsouthbank.daos;

import com.revature.northsouthbank.models.AppUser;
import com.revature.northsouthbank.util.collections.List;
import com.revature.northsouthbank.util.datasource.ConnectionFactory;

import java.sql.*;
import java.util.UUID;

public class AppUserDAO implements CrudDAO<AppUser> {

    public AppUser findUserByUsername(String username) {

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            String sql = "select * from bank_accounts where username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                AppUser user = new AppUser();
                user.setId(rs.getString("user_id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setCurrent_balance(rs.getDouble("current_balance"));

                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    public AppUser findUserByEmail(String email) {

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            String sql = "select * from bank_accounts where email = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                AppUser user = new AppUser();
                user.setId(rs.getString("user_id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setCurrent_balance(rs.getDouble("current_balance"));

                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    public AppUser findUserByUsernameAndPassword(String username, String password) {

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            String sql = "select * from bank_accounts where username = ? and password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                AppUser user = new AppUser();
                user.setId(rs.getString("user_id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public AppUser save(AppUser newUser) {

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            newUser.setId(UUID.randomUUID().toString());

            String sql = "insert into bank_accounts (user_id, first_name, last_name, email, username, password, date_created) values (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newUser.getId());
            pstmt.setString(2, newUser.getFirstName());
            pstmt.setString(3, newUser.getLastName());
            pstmt.setString(4, newUser.getEmail());
            pstmt.setString(5, newUser.getUsername());
            pstmt.setString(6, newUser.getPassword());
            pstmt.setDate(7, (Date) newUser.getCurrent_date());
            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted != 0) {
                return newUser;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<AppUser> findAll() {
        return null;
    }

    @Override
    public AppUser findByID(String id) {
        return null;
    }

    @Override
    public boolean update(AppUser updatedOBJ) {

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            String sql = "update bank_accounts " +
                        "set current_balance = ? " +
                        "where id = ?;";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, updatedOBJ.getId());
            pstmt.setString(7, Double.toString(updatedOBJ.getCurrent_balance()));

            pstmt.executeUpdate();


            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean removeById(String id) {
        return false;
    }
}
