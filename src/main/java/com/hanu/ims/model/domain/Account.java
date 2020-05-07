package com.hanu.ims.model.domain;

public class Account {
    private int id;
    private String username;
    private String password;
    private Role role;

    public Account(int id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Account(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    //    public Account(int id, String username, String password, int role) { // not robust
//        this.id = id;
//        this.username = username;
//        this.password = password;
//        if(role == 1){
//            this.role= Role.Admin;
//        }
//        else if(role == 2){
//            this.role= Role.InventoryManager;
//        }
//        else{
//            this.role= Role.Salesperson;
//        }
//    }

    public int getId() {
        return id;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
