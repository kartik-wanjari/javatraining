package com.azuga.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Constituents {

    @Id
    private Integer constituentID;
    private String role;
    private String name;
    private String constituentULAN_URL;
    private String constituentWikidata_URL;
    private String gender;


}
