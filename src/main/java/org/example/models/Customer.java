package org.example.models;

public class Customer extends User {
    private String address;
    private String phone;

    public Customer(String username, String password, String address, String phone) {
        super(username, password, org.example.enums.UserRole.CUSTOMER);
        this.address = address;
        this.phone = phone;
    }

    public Customer() {
        super();
    }

    // Getters and setters
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public void displayCapabilities() {
        System.out.println("Customer Capabilities: Browse Books, Manage Cart, Place Orders, and Write Reviews.");
    }

    @Override
    public String toString() {
        return super.toString() + ", address='" + address + '\'' + ", phone='" + phone + '\'';
    }
}