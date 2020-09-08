package com.freightmate.harbour.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@SuperBuilder
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Suburb extends BaseEntity<Long> {

    private String name;
    private Integer postcode;
    private String state;
    private String country;

    public Suburb() {
        this.isDeleted = false;
    }

    /**
     * Convert Suburb to string formatted like '(postcode,suburb, userId)' for db insert
     * @return
     */
    public String toSqlValueString(long userId) {
        StringBuilder builder = new StringBuilder();
        return builder
                .append("(")
                .append(this.postcode)
                .append(",'")
                .append(this.name.replace("'","").replace("O\\'","")) // handle apostrophes in the name
                .append("',")
                .append(userId) // set the user id for the person running the script
                .append(")")
                .toString();
    }

    /**
     * Suburb names are not unique, and neither are postcodes. We need a unique ID for them. it will be name and postcode concatted
     * eg Suburb(Melbourne,3000) -> melbourne3000
     * @return string unique key for a postcode/suburb name pair
     */
    public String getSuburbKey() {
        return this.name.toLowerCase().concat(this.postcode.toString());
    }
}
