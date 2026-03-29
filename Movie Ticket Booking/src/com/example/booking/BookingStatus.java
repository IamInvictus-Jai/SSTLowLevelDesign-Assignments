package com.example.booking;

public enum BookingStatus {
    PENDING,    // seats locked, awaiting payment
    CONFIRMED,  // payment successful
    CANCELLED   // cancelled by user or lock expired
}
