/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.pod.server;

import io.github.pod.entidades.Person;
import io.github.pod.entidades.Product;
import io.github.pod.persistencia.PersonDbDao;
import io.github.pod.persistencia.ProductDbDao;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
public class ServerThread extends Thread implements Runnable {

    private Socket clientSocket;

    public ServerThread(Socket clientSocket) {

        this.clientSocket = clientSocket;
    }

    public ServerThread(Socket clientSocket, String threadName) {

        super(threadName);
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

        try {

            InputStream clientIn = clientSocket.getInputStream();

            while (clientSocket.isConnected()) {

                if (clientIn.available() > 0) {
                    byte[] clientMessage = new byte[clientIn.available()];

                    clientIn.read(clientMessage);

                    processMessage(clientMessage);
                }

                sleep(100);
            }

        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }

    }

    public void disconnect() throws IOException {

        clientSocket.close();
    }

    private void processMessage(byte[] clientMessage) throws IOException {

        String fullClientMessage = new String(clientMessage);

        String[] messageParts = fullClientMessage.split(":");

        String reply = "";

        OutputStream clienteOut = clientSocket.getOutputStream();

        try {
            switch (messageParts[0]) {

                case "1": {

                    Product product = new Product();
                    product.setId(messageParts[1]);
                    product.setName(messageParts[2]);

                    saveProduct(product);

                    reply = fullClientMessage;
                }
                break;

                case "2": {

                    Person person = new Person();
                    person.setId(messageParts[1]);
                    person.setName(messageParts[2]);

                    savePerson(person);

                    Socket socketNode2 = new Socket("localhost", 40998);
                    
                    socketNode2.getOutputStream().write(fullClientMessage.getBytes());

                    reply = fullClientMessage;
                    
                    System.out.println("salvei e mandei "+fullClientMessage);
                }
                break;

                case "3": {

                    if (existProduct(messageParts[3])) {
                        reply = fullClientMessage;
                        System.out.println("setei msg");
                    } else {
                        reply = "produto inexistente";
                    }
                    
                }

            }
        } catch (SQLException ex) {

            reply = "0";
        } finally {

            clienteOut.write(reply.getBytes());
            System.out.println("respondi ");
        }

    }

    private boolean existProduct(String productId) throws SQLException {

        ProductDbDao dao = new ProductDbDao();

        return dao.getProduct(productId) != null;

    }

    private void savePerson(Person person) throws SQLException {

        PersonDbDao dao = new PersonDbDao();

        dao.save(person);
    }

    private void saveProduct(Product product) throws SQLException {

        ProductDbDao dao = new ProductDbDao();

        dao.save(product);
    }

}
