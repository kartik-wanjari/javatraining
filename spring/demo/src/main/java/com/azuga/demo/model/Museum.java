package com.azuga.demo.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Museum {

    @Id
    private Integer objectID;


    @OneToMany(cascade = CascadeType.ALL)
    private List<Measurements> measurements;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Constituents> constituents;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Tags> tags;

    private Integer objectEndDate;
    private Integer objectBeginDate;

    @ElementCollection
    private List<String> additionalImages;

    private Boolean isHighlight;
    private Boolean isTimelineWork;
    private Boolean isPublicDomain;

    private String artistBeginDate;
    private String artistDisplayName;
    private String country;
    private String objectDate;
    private String geographyType;
    private String objectURL;
    private String reign;
    private String county;
    private String artistGender;
    private String repository;
    private String dynasty;
    private String portfolio;
    private String excavation;
    private String state;
    private String artistAlphaSort;
    private String period;
    private String primaryImage;
    private String subregion;
    private String classification;
    private String objectWikidataURL;
    private String accessionYear;
    private String region;
    private String primaryImageSmall;
    private String artistSuffix;
    private String city;
    private String linkResource;
    private String artistPrefix;
    private String artistWikidataURL;
    private String medium;
    private String title;
    private String locale;
    private String accessionNumber;
    private String artistEndDate;
    private String rightsAndReproduction;
    private String metadataDate;
    private String creditLine;
    private String artistRole;
    private String department;
    private String galleryNumber;
    private String artistULANURL;
    private String culture;
    private String artistNationality;
    private String objectName;
    private String artistDisplayBio;
    private String locus;
    private String river;
    private String dimensions;


}