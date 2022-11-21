package com.azuga.demo.controller;

import com.azuga.demo.exception.ObjectIdNotFoundException;
import com.azuga.demo.model.Museum;
import com.azuga.demo.service.MuseumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class Controller {

    @Autowired
    MuseumService museumService;

    @GetMapping("hi")
    public ResponseEntity<String> helloWorld(){
        return new ResponseEntity<>("Hello world!!", HttpStatus.OK);
    }

    @GetMapping("show-all-arts")
    public ResponseEntity<List<Museum>> allArts(){
        return new ResponseEntity<>(museumService.showAllArts(), HttpStatus.OK);
    }
    @GetMapping("show/{id}")
    public ResponseEntity<Museum> get(@PathVariable("id") Integer objectID) throws ObjectIdNotFoundException {
        return new ResponseEntity<>(museumService.findById(objectID), HttpStatus.OK);
    }

    @PostMapping("add-art")
    public ResponseEntity<Museum> addArt(@RequestBody Museum museum){
        return new ResponseEntity<>(museumService.addArt(museum), HttpStatus.OK);
    }

    @PutMapping("update")
    public ResponseEntity<String> update(@RequestBody Museum museum) throws ObjectIdNotFoundException {
        return new ResponseEntity<>(museumService.updateArt(museum), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Integer museumID) throws ObjectIdNotFoundException {
        return new ResponseEntity<>(museumService.deleteArt(museumID), HttpStatus.OK);
    }
}
