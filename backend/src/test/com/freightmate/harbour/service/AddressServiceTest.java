package com.freightmate.harbour.service;

import com.freightmate.harbour.Harbour;
import com.freightmate.harbour.model.Address;
import com.freightmate.harbour.model.AddressType;
import com.freightmate.harbour.model.Suburb;
import com.freightmate.harbour.model.dto.AddressDTO;
import com.freightmate.harbour.model.UserRole;
import com.freightmate.harbour.model.dto.SuburbDTO;
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
        this.newAddress.getSuburb().setName("WANTIRNA SOUTH");
        this.newAddress.getSuburb().setPostcode(3152);
        this.newAddress.getSuburb().setState("VIC");
        this.newAddress.setCompany("TEST");
        this.newAddress.setContactName("Smith");
        this.newAddress.setEmail("smith@foo.bar");
        this.newAddress.setPhoneNumber("9800 1234");
    }

    @Test
    @Ignore
    public void addressServiceShouldSucceed_WhenCreatingNewAddress() {
        Address result = this.addressService.createAddress(newAddress, 533);
        assert this.newAddress.getAddressLine1().equals(result.getAddressLine1());
    }

    @Test
    @Ignore
    public void addressServiceShouldSucceed_WhenGettingAddressesForAUser() {
        PageRequest pageRequest = PageRequest.of(0,10);
        List<Address> result = this.addressService.readAddress(533, UserRole.CLIENT, AddressType.DELIVERY, pageRequest);
        assert result.size() > 0;
    }
    
    @Test
    @Ignore
    public void addressServiceShouldSucceed_WhenUpdatingAddress() {
        SuburbDTO suburbDto = SuburbDTO.builder()
                .id(1)
                .name("SOUTHBANK")
                .postcode(3006)
                .state("VIC")
                .country("AUSTRALIA")
                .build();
        AddressDTO updatedAddress = AddressDTO.builder()
                .id(this.newAddress.getId())
                .addressType(AddressType.DELIVERY)
                .addressLine1("10 SMITH ST")
                .suburb(suburbDto)
                .company("TEST")
                .contactName("Smith")
                .email("smith@foo.bar")
                .phoneNumber("9800 1234")
                .build();

        Address result = this.addressService.updateAddress(updatedAddress, newAddress, 533);
        assert result.getSuburb().getName().equals(updatedAddress.getSuburb().getName());
    }

    @Test
    @Ignore
    @Transactional
    public void addressServiceShouldSucceed_WhenDeletingAnAddress() {
        List<Long> ids = new ArrayList();
        ids.add(this.newAddress.getId());
        this.addressService.deleteAddresses(ids, 533, UserRole.CLIENT);

        assert Objects.isNull(
                this.addressService.getAddresses(
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
