package com.freightmate.harbour.service;

import com.freightmate.harbour.Harbour;
import com.freightmate.harbour.model.Address;
import com.freightmate.harbour.model.AddressQueryResult;
import com.freightmate.harbour.model.AddressType;
import com.freightmate.harbour.model.dto.AddressDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Harbour.class)
public class AddressServiceTest {
    @Autowired
    private AddressService addressService;

    private Address newAddress;

    @Before
    public void init() {
        this.newAddress = new Address();
        this.newAddress.setReferenceId("");
        this.newAddress.setAddressType(AddressType.DELIVERY);
        this.newAddress.setAddressLine1("10 SMITH ST");
        this.newAddress.setTown("WANTIRNA SOUTH");
        this.newAddress.setPostcode(3152);
        this.newAddress.setState("VIC");
        this.newAddress.setCompanyName("TEST");
        this.newAddress.setContactName("Smith");
        this.newAddress.setContactEmail("smith@foo.bar");
        this.newAddress.setContactNo("9800 1234");
    }

    @Test
    @Ignore
    public void addressServiceShouldSucceed_WhenCreatingNewAddress() {
        Address result = this.addressService.createAddress(newAddress, "kurtis");
        assert this.newAddress.getAddressLine1().equals(result.getAddressLine1());
    }

    @Test
    @Ignore
    public void addressServiceShouldSucceed_WhenGettingAddressesForAUser() {
        PageRequest pageRequest = PageRequest.of(0,10);
        AddressQueryResult result = this.addressService.readAddress("kurtis", AddressType.DELIVERY, pageRequest);
        assert result.getCount() > 0;
    }
    
    @Test
    @Ignore
    public void addressServiceShouldSucceed_WhenUpdatingAddress() {
        AddressDto updatedAddress = new AddressDto();
        updatedAddress.setId(this.newAddress.getId());
        updatedAddress.setAddressType(AddressType.DELIVERY);
        updatedAddress.setAddressLine1("10 SMITH ST");
        updatedAddress.setTown("SOUTHBANK");
        updatedAddress.setPostcode(3006);
        updatedAddress.setState("VIC");
        updatedAddress.setCompanyName("TEST");
        updatedAddress.setContactName("Smith");
        updatedAddress.setContactEmail("smith@foo.bar");
        updatedAddress.setContactNo("9800 1234");

        Address result = this.addressService.updateAddress(updatedAddress, newAddress);
        assert result.getTown().equals(updatedAddress.getTown());
    }

    @Test
    @Ignore
    @Transactional
    public void addressServiceShouldSucceed_WhenDeletingAnAddress() {
        List<Long> ids = new ArrayList();
        ids.add(this.newAddress.getId());
        this.addressService.deleteAddress(ids, "kurtis");

        assert Objects.isNull(
                this.addressService.getAddressesByIds(
                        Collections.singletonList(
                                this.newAddress.getId()
                        )
                )
        );
    }

    @After
    public void cleanUp() {
        // todo: delete from table
    }

}
