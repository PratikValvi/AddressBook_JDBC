package com.bridgelabz.addressbookservice;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class AddressBookDBService {
    private static AddressBookDBService addressBookDBService;

    private AddressBookDBService() {

    }

    public static AddressBookDBService getInstance() {
        if(addressBookDBService == null)
            addressBookDBService = new AddressBookDBService();
        return addressBookDBService;
    }

    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/addressbook_db?useSSL=false";
        String userName = "root";
        String password = "root";
        Connection connection;
        System.out.println("Connecting to database:" + jdbcURL);
        connection = DriverManager.getConnection(jdbcURL, userName, password);
        System.out.println("Connection is successful!!!" + connection);
        return connection;
    }

    public List<Person> readData() {
        String sql = "SELECT * FROM addressbook_table;";
        return this.getPersonDatafromDatabase(sql);
    }

    private List<Person> getPersonDatafromDatabase(String query) {
        List<Person> personList = new ArrayList<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            personList = this.getPersonData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return personList;
    }

    private List<Person> getPersonData(ResultSet resultSet) {
        List<Person> personList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String address = resultSet.getString("address");
                String city = resultSet.getString("city");
                String state = resultSet.getString("state");
                String zip = resultSet.getString("zip");
                String mobileNumber = resultSet.getString("mobileNumber");
                String email = resultSet.getString("email");
                LocalDate entryDate = resultSet.getDate("entryDate").toLocalDate();
                personList.add(new Person(id, firstName, lastName, address, city, state, zip, mobileNumber, email, entryDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return personList;
    }
}
