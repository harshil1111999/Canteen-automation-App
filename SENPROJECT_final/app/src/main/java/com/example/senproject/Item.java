package com.example.senproject;

public class Item {
    private String Name;
    private String Price;
    private String Acailability;

    public Item(){

    }

    public Item(String name, String price, String acailability) {
        Name = name;
        Price = price;
        Acailability = acailability;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public void setAcailability(String acailability) {
        Acailability = acailability;
    }

    public String getName() {
        return Name;
    }

    public String getPrice() {
        return Price;
    }

    public String getAcailability() {
        return Acailability;
    }
}
