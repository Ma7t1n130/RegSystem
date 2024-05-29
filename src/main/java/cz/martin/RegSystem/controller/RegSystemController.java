package cz.martin.RegSystem.controller;

import cz.martin.RegSystem.model.PersonCreateStructure;
import cz.martin.RegSystem.model.PersonUpdateStructure;
import cz.martin.RegSystem.service.DataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class RegSystemController {

    @Autowired
    DataManager dataManager;

    @PostMapping("user")
    public ResponseEntity<String> createPerson(
            @RequestBody PersonCreateStructure personCreate
    ){
        try{
            dataManager.createPerson(personCreate);
            return new ResponseEntity<>("Person successfully created.", HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>("Error: "+e.getLocalizedMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("user/{id}")
    public ResponseEntity<String> getUser(
            @PathVariable String id,
            @RequestParam(value = "detail", required = false) boolean detail
    ){
        try{
            HttpHeaders httpHeaders=new HttpHeaders();
            httpHeaders.add("Content-Type","application/json");
            return new ResponseEntity<>(dataManager.getUser(id,detail), httpHeaders, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>("Error: "+e.getLocalizedMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("users")
    public ResponseEntity<String> getUser(
            @RequestParam(value = "detail", required = false) boolean detail
    ){
        try{
            HttpHeaders httpHeaders=new HttpHeaders();
            httpHeaders.add("Content-Type","application/json");
            return new ResponseEntity<>(dataManager.getUsers(detail), httpHeaders, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>("Error: "+e.getLocalizedMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("user")
    public ResponseEntity<String> updateUser(
            @RequestBody PersonUpdateStructure personUpdate
    ){
        try{
            dataManager.updatePerson(personUpdate);
            return new ResponseEntity<>("Person successfully updated.", HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>("Error: "+e.getLocalizedMessage(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("user/{id}")
    public ResponseEntity<String> delUser(
            @PathVariable String id
    ){
        try{
            dataManager.deletePerson(id);
            return new ResponseEntity<>("Person successfully deleted.", HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>("Error: "+e.getLocalizedMessage(), HttpStatus.CONFLICT);
        }
    }

}
