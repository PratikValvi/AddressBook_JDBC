package com.bridgelabz.addressbookservice;

import org.junit.jupiter.api.Test;
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
}
