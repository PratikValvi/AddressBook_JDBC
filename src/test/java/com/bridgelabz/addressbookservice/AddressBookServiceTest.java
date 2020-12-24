package com.bridgelabz.addressbookservice;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.sql.Array;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
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
        addressBookService.addEmployeeToAddressBook(7,"James", "Bond",
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
}
