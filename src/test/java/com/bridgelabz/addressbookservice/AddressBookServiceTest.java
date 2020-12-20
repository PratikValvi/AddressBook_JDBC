package com.bridgelabz.addressbookservice;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AddressBookServiceTest {

    @Test
    public void givenAddressBookInDB_WhenRetrieved_ShouldMatchPersonCount() {
        AddressBookService addressBookService = new  AddressBookService();
        List<Person> addressBookDataList = addressBookService.readAddressBookData(AddressBookService.IOService.DB_IO);
        assertEquals(3, addressBookDataList.size());
    }

    @Test
    public void givenNewMobileNumberForEmployee_WhenUpdated_ShouldSyncWithDB() {
        AddressBookService addressBookService = new AddressBookService();
        List<Person> personList = addressBookService.readAddressBookData(AddressBookService.IOService.DB_IO);
        addressBookService.updateMobileNumber("Pratik","9595260222");
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
        assertEquals(2, addressBookDataList.size());
    }
}
