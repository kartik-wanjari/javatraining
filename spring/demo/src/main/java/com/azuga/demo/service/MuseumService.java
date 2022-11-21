package com.azuga.demo.service;

import com.azuga.demo.exception.ObjectIdNotFoundException;
import com.azuga.demo.model.Museum;

import java.util.List;

public interface MuseumService {
    public List<Museum> showAllArts();
    public String updateArt(Museum museum) throws ObjectIdNotFoundException;
    public String deleteArt(Integer objectId) throws ObjectIdNotFoundException;
    public Museum addArt(Museum museum);
    public Museum findById(Integer objectID) throws ObjectIdNotFoundException;
}
