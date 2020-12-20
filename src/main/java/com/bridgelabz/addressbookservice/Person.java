package com.bridgelabz.addressbookservice;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public class Person {
    int id;
    String firstName;
    String lastName;
    String address;
    String city;
    String state;
    String zip;
    String mobileNumber;
    String email;
    LocalDate entryDate;

    public Person(int id, String firstName, String lastName, String address, String city, String state, String zip, String mobileNumber, String email, LocalDate entryDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.entryDate = entryDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    @Override
    public String toString() {
        String details = null;
        details = "\nID: " + id +
                "\nFirst Name: " + firstName +
                "\nLast Name: " + lastName +
                "\nAddress: " + address +
                "\nCity: " + city +
                "\nState: " + state +
                "\nZip: " + zip +
                "\nMobile Number: " + mobileNumber +
                "\nEmail: " + email +
                "\nEntry Date: " + entryDate;
        return details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return id == person.id && Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(address, person.address) && Objects.equals(city, person.city) && Objects.equals(state, person.state) && Objects.equals(zip, person.zip) && Objects.equals(mobileNumber, person.mobileNumber) && Objects.equals(email, person.email) && Objects.equals(entryDate, person.entryDate);
    }
}
