package com.freightmate.harbour.model;

import org.junit.Test;

import java.util.Objects;

public class AddressTest {

    // A very simple test
    @Test
    public void AddressBookShouldSucceed_WhenConstructingNewAddress() {
        Address newAddress = Address.builder()
                .companyName("Smithsonian")
                .town("MELBOURNE")
                .postcode(3000)
                .state(AddressState.VIC.toString())
                .contactName("Mr. Smith")
                .build();

        assert Objects.nonNull(newAddress);
    }
}
