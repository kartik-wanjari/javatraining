package com.azuga.demo.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tags {
    @Id
    @GeneratedValue
    private Integer termId;
    private String term;
    private String AAT_URL;
    private String Wikidata_URL;

}
