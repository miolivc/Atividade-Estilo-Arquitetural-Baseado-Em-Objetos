/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.pod.persistencia;

import io.github.pod.entidades.Product;
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
public class ProductDbDao {

    public boolean save(Product product) throws SQLException {

        Connection connection = DbConnector.getConnection();
        
        String sql = "INSERT INTO product (id, name) VALUES (?, ?)";

        try {
            
            PreparedStatement prepareStatement = connection.prepareStatement(sql);

            int count = 1;
            
            prepareStatement.setString(count++, product.getId());
            prepareStatement.setString(count++, product.getName());

            prepareStatement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }finally{
            connection.close();
        }
        
        return true;
    }
    
    public List<Product> listAll() throws SQLException {

        Connection connection = DbConnector.getConnection();

        String sql = "SELECT * FROM product";

        try {

            PreparedStatement prepareStatement = connection.prepareStatement(sql);

            ResultSet resultQuery = prepareStatement.executeQuery();
            
            List<Product> products = Collections.emptyList();
            
            while(resultQuery.next()){
                products.add(buildProduct(resultQuery));
            }
            
            return products;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        } finally {
            connection.close();
        }
        
    }
    
    public Product getProduct(String productId) throws SQLException{
        
        Connection connection = DbConnector.getConnection();

        String sql = "SELECT * FROM product WHERE id = ?";

        try {

            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            
            prepareStatement.setString(1, productId);

            ResultSet resultQuery = prepareStatement.executeQuery();
            
            System.out.println(sql+"  "+productId);
            
            if(resultQuery.next())
            return buildProduct(resultQuery);
            
            return null;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            connection.close();
        }
    
    }

    private Product buildProduct(ResultSet resultQuery) throws SQLException {
        
        Product product = new Product();
        
        product.setId(resultQuery.getString("id"));
        product.setName(resultQuery.getString("name"));
        
        return product;
    }

}
