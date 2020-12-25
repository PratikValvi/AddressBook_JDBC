package com.bridgelabz.addressbookservice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class AddressBookService {

    public enum IOService {DB_IO,REST_IO,FILE_IO}
    List<Person> personList;
    List<Person> newPersonList = new ArrayList<>();
    private AddressBookDBService addressBookDBService;

    public AddressBookService () {
        addressBookDBService = AddressBookDBService.getInstance();
    }

    public AddressBookService(List<Person> personList) {
        this();
        this.personList = personList;
    }

    public List<Person> readAddressBookData(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            this.personList = addressBookDBService.readData();
        return personList;
    }

    public void updateMobileNumber(String firstName, String mobileNumber) {
        int result = addressBookDBService.updateMobileNumber(firstName,mobileNumber);
        if (result == 0) return;
        Person person = this.getPersonData(firstName);
        if (person != null) person.mobileNumber = mobileNumber;
    }

    public Person getPersonData(String firstName) {
        return this.personList.stream()
                .filter(addressBookDataItem -> addressBookDataItem.firstName.equals(firstName))
                .findFirst()
                .orElse(null);
    }

    public boolean checkAddressBookInSyncWithDB(String firstName) {
        List<Person> personList = addressBookDBService.getPersonData(firstName);
        Person p1 = personList.get(0);
        Person p2 = getPersonData(firstName);
        return p1.equals(p2);
    }

    public List<Person> readAddressBookForDateRange(IOService ioService, LocalDate startDate, LocalDate endDate) {
        if(ioService.equals(IOService.DB_IO))
            return addressBookDBService.getAddressBookForDateRange(startDate, endDate);
        return null;
    }

    public List<Person> countPeopleFromGivenCity(IOService ioService, String city) {
        return addressBookDBService.countPeopleFromGivenCity(city);
    }

    public void addPersonToAddressBook(int id, String firstName, String lastName, String address, String city, String state, String zip, String mobileNumber, String email, LocalDate entryDate) {
        personList.add(addressBookDBService.addEntryToPayroll(id, firstName, lastName, address, city, state, zip, mobileNumber, email, entryDate));
    }

    public boolean checkNameInDatabase(int id) {
        boolean status = false;
        for (Person person : personList) {
            if (person.getId() == id) {
                status = true;
            }
        }
        return status;
    }

    public void addMultipleRecordsToAddressBook(List<Person> List) {
        List.forEach(person -> {
            System.out.println("Person Being Added: " + person.getFirstName());
            this.addPersonToAddressBook(person.getId(),
                    person.getFirstName(),
                    person.getLastName(),
                    person.getAddress(),
                    person.getCity(),
                    person.getState(),
                    person.getZip(),
                    person.getMobileNumber(),
                    person.getEmail(),
                    person.getEntryDate());
            System.out.println("Person Added: " + person.getFirstName());
            //System.out.println(personList);
        });
    }

    public void addMultipleRecordsUsingThreadToAddressBook(List<Person> List) {
        Map<Integer, Boolean> personAdditionStatus = new HashMap<>();
        List.forEach(person -> {
            Runnable task = () -> {
                personAdditionStatus.put(person.hashCode(), false);
                System.out.println("Person Being Added: " + Thread.currentThread().getName());
                this.addPersonToAddressBook(person.getId(),
                        person.getFirstName(),
                        person.getLastName(),
                        person.getAddress(),
                        person.getCity(),
                        person.getState(),
                        person.getZip(),
                        person.getMobileNumber(),
                        person.getEmail(),
                        person.getEntryDate());
                personAdditionStatus.put(person.hashCode(), true);
                System.out.println("Person Added: " + Thread.currentThread().getName());
            };
            Thread thread = new Thread(task, person.firstName);
            thread.start();
        });
        while (personAdditionStatus.containsValue(false)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {

            }
        }
    }

    public long countEntries(IOService ioService) {
        //if (ioService.equals(IOService.FILE_IO))
        //    return new AddressBookFileIOService().countEntries();
        return personList.size()+newPersonList.size();
    }

    public void addPersonToAddressBook(Person person, IOService ioService) {
        if (ioService.equals(IOService.DB_IO)) {
            this.addPersonToAddressBook(person.id, person.firstName, person.lastName, person.address, person.city, person.state, person.zip, person.mobileNumber, person.email, person.entryDate);
        } else {
            newPersonList.add(person);
        }
    }

    public void updateMobileNumber(String firstName, String mobileNumber, IOService ioService) {
        if(ioService.equals(IOService.DB_IO)) {
            int result = addressBookDBService.updateMobileNumber(firstName, mobileNumber);
            if (result == 0) return;
        }
        Person person = this.getPersonData(firstName);
        if (person != null) person.mobileNumber = mobileNumber;
    }

    public void updateContactNumber(String firstName, String mobileNumber) {
        int result = addressBookDBService.updateMobileNumber(firstName,mobileNumber);
        if (result == 0) return;
        Person person = this.getPersonData(firstName);
        if (person != null) person.mobileNumber = mobileNumber;
    }
}
