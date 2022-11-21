package com.azuga.demo.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Measurements {

    @Id
    @GeneratedValue
    private Integer measurementId;

    private String elementName;
    private String elementDescription;
    @OneToOne(cascade = CascadeType.ALL)
    private ElementMeasurements elementMeasurements;

}
