package com.bridgelabz.addressbookservice;

import java.time.LocalDate;
import java.util.List;

class AddressBookService {

    public enum IOService {DB_IO}
    private List<Person> personList;
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
        Person person = this.getAddressBookData(firstName);
        if (person != null) person.mobileNumber = mobileNumber;
    }

    private Person getAddressBookData(String firstName) {
        return this.personList.stream()
                .filter(addressBookDataItem -> addressBookDataItem.firstName.equals(firstName))
                .findFirst()
                .orElse(null);
    }

    public boolean checkAddressBookInSyncWithDB(String firstName) {
        List<Person> personList = addressBookDBService.getPersonData(firstName);
        for (Person person : personList) {
            person.toString();
        }
        Person p1 = personList.get(0);
        Person p2 = getAddressBookData(firstName);
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
}
