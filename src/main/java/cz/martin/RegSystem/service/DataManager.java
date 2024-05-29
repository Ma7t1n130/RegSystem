package cz.martin.RegSystem.service;

import cz.martin.RegSystem.RegSystemException;
import cz.martin.RegSystem.model.Person;
import cz.martin.RegSystem.model.PersonCreateStructure;
import cz.martin.RegSystem.model.PersonUpdateStructure;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class DataManager {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public DataManager() {
    }

    private int checkPersonNumber(String personId) throws RegSystemException {
        int idPersonNumber;
        //
        try {
            idPersonNumber = (int)jdbcTemplate.queryForObject(
                            "SELECT IDPersonNumber FROM PersonNumber WHERE PersonNumber=?",
                                new Object[]{personId},
                                Integer.class);
        }catch(EmptyResultDataAccessException e){
            throw new RegSystemException("Unknown person number.");
        }
        //
        int exists=(int)jdbcTemplate.queryForObject(
                    "SELECT COUNT(1) FROM Person WHERE IDPersonNumber=?",
                        new Object[]{idPersonNumber},
                        Integer.class);
        if(exists>0){
            throw new RegSystemException("Person number already used.");
        }
        //
        return idPersonNumber;
    }

    private String getUnusedUUID() {
        UUID uuid;
        int exist;
        do{
            uuid=UUID.randomUUID();
            exist=(int)jdbcTemplate.queryForObject("SELECT COUNT(1) FROM Person WHERE UUID=?",
                                                        new Object[]{uuid.toString()},
                                                        Integer.class);
        }
        while(exist!=0);
        return uuid.toString();
    }

    public void createPerson(PersonCreateStructure personCreate) throws RegSystemException{
        try {
            int rows=jdbcTemplate.update("INSERT INTO Person(Name,Surname,IDPersonNumber,UUID) VALUES(?,?,?,?)",
                        personCreate.getName(),
                        personCreate.getSurname(),
                        this.checkPersonNumber(personCreate.getPersonId()),
                        this.getUnusedUUID());
        }catch(Exception e){
            throw new RegSystemException("Create person failed: "+e.getLocalizedMessage());
        }
    }

    private Person selectPerson(String id) throws RegSystemException {
        try {
            Person person = jdbcTemplate.queryForObject("SELECT P.*,N.PersonNumber FROM Person P,PersonNumber N WHERE P.IDPersonNumber=N.IDPersonNumber AND P.IDPerson=?",
                            new Object[]{id},
                            (rs, rowNum) ->
                                    new Person(rs.getInt("IDPerson"),
                                            rs.getString("Name"),
                                            rs.getString("Surname"),
                                            rs.getString("PersonNumber"),
                                            rs.getString("UUID")
                                    )
            );
            return person;
        }catch(EmptyResultDataAccessException e){
            throw new RegSystemException("Unknown id.");
        }
    }

    public String getUser(String id,boolean detail) throws RegSystemException {
        Person person=selectPerson(id);
        JSONObject jsonPerson = new JSONObject();
        jsonPerson.put("id",person.getId());
        jsonPerson.put("name",person.getName());
        jsonPerson.put("surname",person.getSurname());
        if(detail){
            jsonPerson.put("personID",person.getPersonId());
            jsonPerson.put("uuid",person.getUuid());
        }
        return jsonPerson.toString();
    }

    private List<Person> selectUsers(){
        return jdbcTemplate.query("SELECT P.*,N.PersonNumber FROM Person P,PersonNumber N WHERE P.IDPersonNumber=N.IDPersonNumber ORDER BY P.IDPerson",
                (rs, rowNum) -> new Person(rs.getInt("IDPerson"),
                                           rs.getString("Name"),
                                           rs.getString("Surname"),
                                           rs.getString("PersonNumber"),
                                           rs.getString("UUID")
                        )
        );
    }

    public String getUsers(boolean detail){
        JSONArray allUsers = new JSONArray();
        for(Person person:selectUsers()){
            JSONObject oneUser=new JSONObject();
            oneUser.put("id",person.getId());
            oneUser.put("name",person.getName());
            oneUser.put("surname",person.getSurname());
            if(detail){
                oneUser.put("personID",person.getPersonId());
                oneUser.put("uuid",person.getUuid());
            }
            allUsers.put(oneUser);
        }
        return allUsers.toString();
    }

    public void updatePerson(PersonUpdateStructure personUpdate) throws RegSystemException{
        try {
            Person person=selectPerson(String.valueOf(personUpdate.getId())); //jen test zda existuje ID
            int rows=jdbcTemplate.update("UPDATE Person SET Name=?,Surname=? WHERE IDPerson=?",
                        personUpdate.getName(),
                        personUpdate.getSurname(),
                        personUpdate.getId());
        }catch(Exception e){
            throw new RegSystemException("Update person failed: "+e.getLocalizedMessage());
        }
    }

    public void deletePerson(String id) throws RegSystemException{
        try {
            Person person=selectPerson(String.valueOf(id)); //jen test zda existuje ID
            int rows=jdbcTemplate.update("DELETE FROM Person WHERE IDPerson=?",id);
        }catch(Exception e){
            throw new RegSystemException("Update person failed: "+e.getLocalizedMessage());
        }
    }

}
