package com.revature.banking.orm.models;


import com.revature.banking.orm.annotation.ColumnInORM;
import com.revature.banking.orm.annotation.DataSourceORM;
import com.revature.banking.orm.annotation.NotIntoDabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@DataSourceORM(TableName = "app_users", Schema = "banking")
public class AppUserORM {

    // @ColumnInORM(Constraint = "NOT NULL", Size=5, DefaultValue ="" ,
    // PRIMARY = "Y", UNIQUE = "Y", ForeignKey={"",""}, Check="")
    @ColumnInORM(Constraint = "NOT NULL", PRIMARY = "Y")
    private String user_id;
    @ColumnInORM(Constraint = "NOT NULL", Size = 25, Check = "(first_name)::text <> ''::text")
    private String first_name;

    @ColumnInORM(Constraint = "NOT NULL", Size = 25, Check = "(last_name)::text <> ''::text")
    private String last_name;
    @ColumnInORM(Constraint = "NOT NULL", Size = 255, UNIQUE = "Y", Check = "(email)::text <> ''::text")
    private String email;
    @ColumnInORM(Constraint = "NOT NULL", Size = 255, UNIQUE = "Y", Check = "(username)::text <> ''::text")
    private String username;
    @ColumnInORM(Constraint = "NOT NULL", Size = 255, Check = "(password)::text <> ''::text")
    private String password;
//    @NotIntoDabase
//    @ColumnInORM(Constraint = "NOT NULL", DefaultValue = "LOCALTIMESTAMP")
//    private LocalDateTime date_added;

    public AppUserORM(String firstName, String lastName, String email, String username, String password) {
        this.first_name = firstName;
        this.last_name = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public AppUserORM(String id, String firstName, String lastName, String email, String username, String password) {
        this(firstName, lastName, email, username, password);
        this.user_id = id;
    }

    public AppUserORM() {
        super();
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        // potentially add validation logic here (but there's really a better place to do this kind of logic)
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public LocalDateTime getDate_added() {
//        return date_added;
//    }
//
//    public void setDate_added(String date_added) {
//        System.out.println(date_added);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        this.date_added = LocalDateTime.parse(date_added, formatter);
//    }

    public String toFileString() {
        return user_id + ":" +
                first_name + ":" +
                last_name + ":" +
                email + ":" +
                username + ":" +
                password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUserORM appUser = (AppUserORM) o;
        return Objects.equals(user_id, appUser.user_id) && Objects.equals(first_name, appUser.first_name) && Objects.equals(last_name, appUser.last_name) && Objects.equals(email, appUser.email) && Objects.equals(username, appUser.username) && Objects.equals(password, appUser.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, first_name, last_name, email, username, password);
    }

    @Override
    public String toString() {
        return "AppUserORM{" +
                "user_id='" + user_id + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
