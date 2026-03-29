package com.example.booking;

import java.util.UUID;

public class User {
    private final String id;
    private final String name;
    private final String email; // unique
    private final String phone;

    public User(String name, String email, String phone) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getId()    { return id; }
    public String getName()  { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    @Override
    public String toString() { return name + "(" + email + ")"; }
}
