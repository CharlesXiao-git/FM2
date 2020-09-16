package com.freightmate.harbour.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@SuperBuilder
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Manifest extends BaseEntity<Long> {

    @ManyToOne(targetEntity = UserClient.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_client_id")
    private UserClient userClient;

    @Column(name = "user_client_id", insertable = false, updatable = false)
    private long userClientId;

    private Boolean goodsPickupStatus;
    private String manifestNumber;
    private String readyTime;
    private String closingTime;
    private String dataStatus;

    @OneToMany(targetEntity = Consignment.class)
    @JoinColumn(name = "manifestId")
    private List<Consignment> consignments;

    public Manifest() {
        this.isDeleted = false;
    }
}
