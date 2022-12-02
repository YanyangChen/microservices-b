package com.ird.camelservices.camelmicroserviceb.beans;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;


@Entity
@Data
@Table(name="NAME_ADDRESS")
public class NameAddress implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String name;

    @Column(name = "house_number")
    public String houseNumber;

    public String city;

    public String province;

    @Column(name = "postal_code")
    public String postalCode;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
