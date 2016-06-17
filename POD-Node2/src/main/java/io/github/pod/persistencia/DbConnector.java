/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package io.github.pod.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author wensttay
 */
public class DbConnector
{
    Connection connection;
    String user = "postgres";
    String password = "12345";
    String driver = "org.postgresql.Driver";
    String url = "jdbc:postgresql://localhost:5432/db_node_2";
    
    public void connectar() throws SQLException{
        try
        {
            if (getConnection() != null && !getConnection().isClosed())
            {
                return;
            }
            
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
    }
    
    public Connection getConnection(){
        return connection;
    }
    
    public void desconectar(){
        try
        {
            if(getConnection() != null && !getConnection().isClosed()){
                this.connection.close();
            }
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }
}