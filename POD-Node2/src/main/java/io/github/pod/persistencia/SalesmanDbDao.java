/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.pod.persistencia;

import io.github.pod.entidades.Salesman;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Wensttay
 */
public class SalesmanDbDao extends DbConnector{

    
    public boolean save(Salesman salesman) throws SQLException {
        
        connectar();
        String sql = "INSERT INTO Salesman (personid, phone) VALUES (?, ?)";

        try {
            PreparedStatement prepareStatement = getConnection().prepareStatement(sql);

            int i = 1;
            
            prepareStatement.setString(i++, salesman.getPersonid());
            prepareStatement.setString(i++, salesman.getPhone());
            
            return (prepareStatement.executeUpdate() > 0);

        } catch (SQLException ex) {
            ex.printStackTrace();

        }finally{
            desconectar();
        }
        return false;
    }
    
    public boolean exists(String personid) throws SQLException {
        
        connectar();
        String sql = "SELECT * FROM Salesman WHERE personid = ?";

        try {
            PreparedStatement prepareStatement = getConnection().prepareStatement(sql);
            
            int i = 1;
            prepareStatement.setString(i++, personid);
            
            return (prepareStatement.executeQuery().next());
        } catch (SQLException ex) {
            ex.printStackTrace();

        }finally{
            desconectar();
        }
        return false;
    }

}
