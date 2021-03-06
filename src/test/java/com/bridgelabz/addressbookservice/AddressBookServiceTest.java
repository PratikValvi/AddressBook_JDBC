package com.bridgelabz.addressbookservice;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AddressBookServiceTest {

    @Test
    public void givenAddressBookInDB_WhenRetrieved_ShouldMatchPersonCount() {
        AddressBookService addressBookService = new  AddressBookService();
        List<Person> addressBookDataList = addressBookService.readAddressBookData(AddressBookService.IOService.DB_IO);
        assertEquals(6, addressBookDataList.size());
    }

    @Test
    public void givenNewMobileNumberForEmployee_WhenUpdated_ShouldSyncWithDB() {
        AddressBookService addressBookService = new AddressBookService();
        List<Person> personList = addressBookService.readAddressBookData(AddressBookService.IOService.DB_IO);
        addressBookService.updateMobileNumber("Pratik","8862007111");
        boolean result = addressBookService.checkAddressBookInSyncWithDB("Pratik");
        assertTrue(result);
    }

    @Test
    public void givenDateRangeWhenRetrieved_ShouldMatchEntryCount() {
        AddressBookService addressBookService = new AddressBookService();
        LocalDate startDate = LocalDate.of(2017, 01, 01);
        LocalDate endDate = LocalDate.now();
        List<Person> addressBookDataList =
                addressBookService.readAddressBookForDateRange(AddressBookService
                        .IOService.DB_IO, startDate, endDate);
        assertEquals(5, addressBookDataList.size());
    }

    @Test
    public void givenCity_WhenRetrieved_ShouldMatchEntryCount() {
        AddressBookService addressBookService = new AddressBookService();
        List<Person> addressBookDataList =
                addressBookService.countPeopleFromGivenCity(AddressBookService
                        .IOService.DB_IO, "Satara");
        assertEquals(4, addressBookDataList.size());
    }

    @Test
    public void givenNewEntry_WhenAdded_ShouldSyncWithDB() {
        AddressBookService addressBookService = new AddressBookService();
        addressBookService.readAddressBookData(AddressBookService.IOService.DB_IO);
        LocalDate entryDate = LocalDate.of(2020, 05, 23);
        addressBookService.addPersonToAddressBook(7,"James", "Bond",
                "Sadar Bazar","Satara", "Maharashtra", "415001", "7777777777","bondjamesbond@gmail.com",entryDate);
        boolean status = addressBookService.checkNameInDatabase(7);
        assertTrue(status);
    }

    @Test
    public void given4Entries_WhenAdded_ShouldGetAdded() {
        Person[] arrayOfPersonsOne = {
                new Person(7,"Jack", "Bond",
                        "Sadar Bazar","Satara", "Maharashtra", "415001", "7777777777","bondjamesbond@gmail.com",LocalDate.now()),
                new Person(8,"Brad", "Bond",
                        "Sadar Bazar","Satara", "Maharashtra", "415001", "7777777777","bondjamesbond@gmail.com",LocalDate.now()),
                new Person(9,"Harry", "Bond",
                        "Sadar Bazar","Satara", "Maharashtra", "415001", "7777777777","bondjamesbond@gmail.com",LocalDate.now()),
                new Person(10,"James", "Bond",
                        "Sadar Bazar","Satara", "Maharashtra", "415001", "7777777777","bondjamesbond@gmail.com",LocalDate.now()),
        };
        AddressBookService addressBookService = new AddressBookService();
        addressBookService.readAddressBookData(AddressBookService.IOService.DB_IO);
        Instant start = Instant.now();
        addressBookService.addMultipleRecordsToAddressBook(Arrays.asList(arrayOfPersonsOne));
        Instant end = Instant.now();
        System.out.println("Duration without Thread: " + Duration.between(start,end));
        boolean status = addressBookService.checkNameInDatabase(10);
        assertTrue(status);
    }

    @Test
    public void given4Entries_WhenAdded_ShouldGetAddedUsingThread() {
        Person[] arrayOfPersonsOne = {
                new Person(7,"Jack", "Bond",
                        "Sadar Bazar","Satara", "Maharashtra", "415001", "7777777777","bondjamesbond@gmail.com",LocalDate.now()),
                new Person(8,"Brad", "Bond",
                        "Sadar Bazar","Satara", "Maharashtra", "415001", "7777777777","bondjamesbond@gmail.com",LocalDate.now()),
                new Person(9,"Harry", "Bond",
                        "Sadar Bazar","Satara", "Maharashtra", "415001", "7777777777","bondjamesbond@gmail.com",LocalDate.now()),
                new Person(10,"James", "Bond",
                        "Sadar Bazar","Satara", "Maharashtra", "415001", "7777777777","bondjamesbond@gmail.com",LocalDate.now()),
        };
        Person[] arrayOfPersonsTwo = {
                new Person(11,"Jack", "Bond",
                        "Sadar Bazar","Satara", "Maharashtra", "415001", "7777777777","bondjamesbond@gmail.com",LocalDate.now()),
                new Person(12,"Brad", "Bond",
                        "Sadar Bazar","Satara", "Maharashtra", "415001", "7777777777","bondjamesbond@gmail.com",LocalDate.now()),
                new Person(13,"Harry", "Bond",
                        "Sadar Bazar","Satara", "Maharashtra", "415001", "7777777777","bondjamesbond@gmail.com",LocalDate.now()),
                new Person(14,"James", "Bond",
                        "Sadar Bazar","Satara", "Maharashtra", "415001", "7777777777","bondjamesbond@gmail.com",LocalDate.now()),
        };
        AddressBookService addressBookService = new AddressBookService();
        addressBookService.readAddressBookData(AddressBookService.IOService.DB_IO);

        Instant start = Instant.now();
        addressBookService.addMultipleRecordsToAddressBook(Arrays.asList(arrayOfPersonsOne));
        Instant end = Instant.now();
        System.out.println("Duration without Thread: " + Duration.between(start,end));
        System.out.println();

        Instant startForThread = Instant.now();
        addressBookService.addMultipleRecordsUsingThreadToAddressBook(Arrays.asList(arrayOfPersonsTwo));
        Instant endForThread = Instant.now();
        System.out.println("Duration with Thread: " + Duration.between(startForThread,endForThread));
        boolean status = addressBookService.checkNameInDatabase(14);
        assertEquals(true, status);
    }

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 3000;
    }

    public Person[] getPersonList() {
        setup();
        Response response = RestAssured.get("/Persons");
        System.out.println("PERSON DATA IN JSON Server :\n" + response.asString());
        Person[] arrayOfPersons = new Gson().fromJson(response.asString(), Person[].class);
        return arrayOfPersons;
    }

    @Test
    public void givenPersonDataInJSONServerWhenRetrivedShouldMatchTheCount(){
        Person[] arrayOfPersons = getPersonList();
        AddressBookService addressBookService;
        addressBookService = new AddressBookService(Arrays.asList(arrayOfPersons));
        long entries = addressBookService.countEntries(AddressBookService.IOService.REST_IO);
        assertEquals(2, entries);
    }

    private Response addPersonToJsonServer(Person person) {
        String personJson = new Gson().toJson(person);
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.header("Content-Type", "application/json");
        requestSpecification.body(personJson);
        return requestSpecification.post("/Persons");
    }

    @Test
    public void givenListOfNewPersonsWhenAddedShouldMatchResponse201AndCount(){
        List<Person> list = new ArrayList<>();
        Person[] arrayOfPersons = getPersonList();
        System.out.println(arrayOfPersons);
        AddressBookService addressBookService;
        addressBookService = new AddressBookService(Arrays.asList(arrayOfPersons));
        Person[] arrayOfPersonData = {
                new Person(3, "Suraj", "Nigave", "Vishrambaug", "Sangli", "Maharashtra", "414414", "8888778811", "suraj.nigave@gmail.com", LocalDate.now()),
                new Person(4, "Roshan", "Valvi", "Mangrul", "Nandurbar", "Maharashtra", "414000", "7777778811", "roshan.valvi@gmail.com", LocalDate.now())
        };
        for(Person person : arrayOfPersonData) {
            Response response = addPersonToJsonServer(person);
            int statusCode = response.getStatusCode();
            assertEquals(201, statusCode);
            person = new Gson().fromJson(response.asString(), Person.class);
            System.out.println(person.toString());
            addressBookService.addPersonToAddressBook(person, AddressBookService.IOService.REST_IO);
        }
        long entries = addressBookService.countEntries(AddressBookService.IOService.REST_IO);
        assertEquals(4, entries);
    }

    @Test
    public void givenNewContactNumberForPersonWhenUpdatedShouldMatch200Response(){
        Person[] arrayOfPersons = getPersonList();
        AddressBookService addressBookService;
        addressBookService = new AddressBookService(Arrays.asList(arrayOfPersons));

        addressBookService.updateMobileNumber("Pratik", "9503836038", AddressBookService.IOService.REST_IO);
        Person person = addressBookService.getPersonData("Pratik");

        String personJson = new Gson().toJson(person);
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.header("Content-Type", "application/json");
        requestSpecification.body(personJson);
        Response response = requestSpecification.put("/Persons/" +person.id);
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void givenPersonToDeleteWhenDeletedShouldMatch200ResponseAndCount(){
        Person[] arrayOfPersons = getPersonList();
        AddressBookService addressBookService;
        addressBookService = new AddressBookService(Arrays.asList(arrayOfPersons));

        Person person = addressBookService.getPersonData("Suraj");
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.header("Content-Type", "application/json");
        Response response = requestSpecification.delete("/Persons/" +person.id);
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        addressBookService.deletePersonFromAddressBook(person.firstName, AddressBookService.IOService.REST_IO);
        long entries = addressBookService.countEntries(AddressBookService.IOService.REST_IO);
        assertEquals(2, entries);
    }
}
