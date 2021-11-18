package com.revature.northsouthbank.models;

import java.util.Date;
import java.util.Objects;

public class AppUser extends Object {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private Double current_balance;
    private Date current_date;
    public AppUser(String firstName, String lastName, String email, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;

    }

    public AppUser(String id, String firstName, String lastName, String email, String username, String password) {
        this(firstName, lastName, email, username, password);
        this.id = id;
        this.current_balance = current_balance;

    }

    public AppUser() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public Double getCurrent_balance() {
        return current_balance;
    }

    public void setCurrent_balance(Double current_balance) {
        this.current_balance = current_balance;
    }

    public Date getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date() {
        this.current_date = current_date;
    }

    public String toFileString() {
        StringBuilder builder = new StringBuilder();
        builder.append(id).append(":")
                .append(firstName).append(":")
                .append(lastName).append(":")
                .append(email).append(":")
                .append(username).append(":")
                .append(password);
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUser appUser = (AppUser) o;
        return Objects.equals(id, appUser.id) && Objects.equals(firstName, appUser.firstName) && Objects.equals(lastName, appUser.lastName) && Objects.equals(email, appUser.email) && Objects.equals(username, appUser.username) && Objects.equals(password, appUser.password);

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, username, password);
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';


    }

}
