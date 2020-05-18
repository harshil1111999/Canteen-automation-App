package com.example.senproject;

public class Canteen {
    private String Name;
    private  String Email;
    private String Password;
    private String Available;
    private String Virtual_Money;

    public Canteen(){

    }

    public Canteen(String available, String email, String name, String password, String virtual_Money) {
        Name = name;
        Email = email;
        Password = password;
        Available = available;
        Virtual_Money = virtual_Money;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public String getAvailable() {
        return Available;
    }

    public String getVirtual_Money() {
        return Virtual_Money;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setAvailable(String available) {
        Available = available;
    }

    public void setVirtual_Money(String virtual_Money) {
        Virtual_Money = virtual_Money;
    }
}
