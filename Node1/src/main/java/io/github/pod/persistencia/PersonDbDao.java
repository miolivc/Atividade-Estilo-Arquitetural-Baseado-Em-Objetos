/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.pod.persistencia;

import io.github.pod.entidades.Person;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
public class PersonDbDao {

    public boolean save(Person person) throws SQLException {

        Connection connection = DbConnector.getConnection();

        String sql = "INSERT INTO person (id, name) VALUES (?, ?)";

        try {

            PreparedStatement prepareStatement = connection.prepareStatement(sql);

            int count = 1;

            prepareStatement.setString(count++, person.getId());
            prepareStatement.setString(count++, person.getName());

            prepareStatement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            connection.close();
        }

        return true;
    }

    public List<Person> listAll() throws SQLException {

        Connection connection = DbConnector.getConnection();

        String sql = "SELECT * FROM person";

        try {

            PreparedStatement prepareStatement = connection.prepareStatement(sql);

            ResultSet resultQuery = prepareStatement.executeQuery();
            
            List<Person> persons = Collections.emptyList();
            
            while(resultQuery.next()){
                persons.add(buildPerson(resultQuery));
            }
            
            return persons;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        } finally {
            connection.close();
        }
        
    }

    private Person buildPerson(ResultSet resultQuery) throws SQLException {
        
        Person person = new Person();
        
        person.setId(resultQuery.getString("id"));
        person.setName(resultQuery.getString("name"));
        
        return person;
    }

}
