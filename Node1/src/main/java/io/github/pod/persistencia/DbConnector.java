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
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
public final class DbConnector {

    private static Connection connection;

    private DbConnector() {}


    public static Connection getConnection() {

        try {

            if (connection == null || connection.isClosed()) {
                buildConnection();
            }

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        return connection;
    }

    private static void buildConnection() throws ClassNotFoundException, SQLException {
        String url = "jdbc:postgresql://localhost:5432/db_node_1";
        String password = "12345";
        String user = "postgres";
        
        Class.forName("org.postgresql.Driver");
        
        connection = DriverManager.getConnection(url, user, password);
    }

}
