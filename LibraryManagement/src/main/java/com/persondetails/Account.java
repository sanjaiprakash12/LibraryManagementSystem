package com.persondetails;

public class Account {
    int account_id;
    int person_id;
    String username;
    String password;
    String accountType;
    public Account()  {
        this.account_id = 0;
        this.person_id = 0;
        this.username = "";
        this.password = "";
        this.accountType = "";
    }

    public Account(int account_id, int person_id, String username, String password, String accountType)  {
        this.account_id = account_id;
        this.person_id = person_id;
        this.username = username;
        this.password = password;
        this.accountType = accountType;
    }
    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public int getPerson_id() {
        return person_id;
    }

    public void setPerson_id(int person_id) {
        this.person_id = person_id;
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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

}
