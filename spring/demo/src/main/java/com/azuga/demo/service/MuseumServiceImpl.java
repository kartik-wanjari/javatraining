package com.azuga.demo.service;

import com.azuga.demo.exception.ObjectIdNotFoundException;
import com.azuga.demo.model.Museum;
import com.azuga.demo.repository.MuseumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MuseumServiceImpl implements MuseumService {

    @Autowired
    MuseumRepository museumRepo;

    @Override
    public List<Museum> showAllArts() {
        return museumRepo.findAll();
    }

    @Override
    public String updateArt(Museum museum) throws ObjectIdNotFoundException {
        Optional<Museum> updateMuseum = museumRepo.findById(museum.getObjectID());
        if(updateMuseum.isPresent()){
            Museum art = updateMuseum.get();
            museumRepo.save(museum);
            return "Updated";
        }else{
            throw new ObjectIdNotFoundException("objectID "+museum.getObjectID()+" does not exist.");
        }
    }

    @Override
    public String deleteArt(Integer objectId) throws ObjectIdNotFoundException {
        Optional<Museum> museum1 = museumRepo.findById(objectId);
        if (museum1.isPresent()) {
            museumRepo.deleteById(objectId);
            return "Museum deleted with objectId: "+objectId;
        } else{
            throw new ObjectIdNotFoundException("objectID "+objectId+" does not exist.");
        }
    }

    @Override
    public Museum addArt(Museum museum) {
        museum.setObjectID(museum.getObjectID());
        return museumRepo.save(museum);
    }

    @Override
    public Museum findById(Integer objectID) throws ObjectIdNotFoundException {
        Optional<Museum> museum1 = museumRepo.findById(objectID);
        if (museum1.isPresent()) {

            return museum1.get();
        } else{
            throw new ObjectIdNotFoundException("ObjectID "+objectID+" not present");
        }
    }
}
