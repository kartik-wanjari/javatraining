package com.azuga.demo.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.criteria.CriteriaBuilder;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElementMeasurements {

    @Id
    @GeneratedValue
    private Integer elementMeasurementId;
    private Float Height;
    private Float Width;
    private Float Dia;
    private Float Diameter;

}
