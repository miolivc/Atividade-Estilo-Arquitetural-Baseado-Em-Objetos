/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.pod.node3.persistencia;

import io.github.pod.node3.entidades.Order;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Natarajan Rodrigues
 */
public class OrderDbDao {

    public boolean save(Order order) throws SQLException {
        Connection connection = DbConnector.getConnection();
        String sql = "INSERT INTO order_table (orderid, salesmanid, productid, quantity) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement prepareStatement = connection.prepareStatement(sql);

            int count = 1;
            prepareStatement.setString(count++, order.getIdPedido());
            prepareStatement.setString(count++, order.getSalesman());
            prepareStatement.setString(count++, order.getProduto());
            prepareStatement.setInt(count++, order.getQuantity());

            prepareStatement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }finally{
            connection.close();
        }
        
        return true;
    }
    
}
