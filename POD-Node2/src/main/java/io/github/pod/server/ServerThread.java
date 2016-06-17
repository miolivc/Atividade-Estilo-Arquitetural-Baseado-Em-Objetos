/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.pod.server;

import io.github.pod.entidades.Salesman;
import io.github.pod.persistencia.SalesmanDbDao;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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

            byte[] clientMessage = new byte[clientIn.available()];

            clientIn.read(clientMessage);

            processMessage(clientMessage);

        } catch (IOException ex) {
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
                case "2": {
                    System.out.println("salvar esse " + fullClientMessage);
                    Salesman salesman = new Salesman();
                    salesman.setPersonid(messageParts[1]);
                    salesman.setPhone(messageParts[3]);

                    saveSalesman(salesman);
                    reply = fullClientMessage;

                }
                break;

                case "3": {
                    if (existsSalesman(messageParts[2])) {
                        Socket socketNode1 = new Socket("localhost", 40999);
                        OutputStream outputStream = socketNode1.getOutputStream();
                        outputStream.write(fullClientMessage.getBytes());
                        outputStream.flush();

                        InputStream inputStream = socketNode1.getInputStream();
                        byte[] bytes = new byte[1024];
                        inputStream.read(bytes);

                        clientSocket.getOutputStream().write(bytes);
                        socketNode1.close();

//                        reply = fullClientMessage;
                        reply = new String(bytes);
                        System.out.println("recebi isso do 1 " + reply);

                    } else {
                        reply = "vendedor inexistente";
                    }

                }
                break;

                case "4": {

                    Socket socketNode1 = new Socket("localhost", 40999);
                    OutputStream outputStream = socketNode1.getOutputStream();

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("2:").append(messageParts[2]).
                            append(":").append(messageParts[3]).
                            append(":").append(messageParts[4]);

                    String mensToNode1 = stringBuilder.toString();
                    outputStream.write(mensToNode1.getBytes());
                    outputStream.flush();

                    InputStream inputStream = socketNode1.getInputStream();
                    byte[] bytes = new byte[1024];
                    inputStream.read(bytes);

                    if (new String(bytes).trim().equals(mensToNode1)) {
                        Salesman salesman = new Salesman();
                        salesman.setPersonid(messageParts[2]);
                        salesman.setPhone(messageParts[4]);

                        saveSalesman(salesman);
                        reply = fullClientMessage;
                    } else {
                        reply = "Erro ao criar pessoa";
                    }

                }
                break;

            }
        } catch (SQLException ex) {

            reply = "Erro SQL";
        } finally {
            clienteOut.write(reply.getBytes());
            clienteOut.flush();
//            clienteOut.close();
//            disconnect();
        }
    }

    private void saveSalesman(Salesman salesman) throws SQLException {

        SalesmanDbDao salesmanDbDao = new SalesmanDbDao();

        salesmanDbDao.save(salesman);
    }

    private boolean existsSalesman(String personId) throws SQLException {
        SalesmanDbDao salesmanDbDao = new SalesmanDbDao();

        return salesmanDbDao.exists(personId);
    }

}
