/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.pod.node3.server;

import io.github.pod.node3.entidades.Order;
import io.github.pod.node3.persistencia.OrderDbDao;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static java.lang.Thread.sleep;
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
    private Socket node2Socket;

    public ServerThread(Socket clientSocket) throws IOException {
        
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
        } catch (SQLException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void disconnect() throws IOException {

        clientSocket.close();
    }

    private void processMessage(byte[] clientMessage) throws IOException, SQLException {

        String fullClientMessage = new String(clientMessage);

        String[] messageParts = fullClientMessage.split(":");

        String reply = "";

        OutputStream clienteOut = clientSocket.getOutputStream();

        switch (messageParts[0]) {

            case "3": {
                //enviar mensagem para node_2 verificar se existe o vendedor
                node2Socket = new Socket("localhost", 40998);
                node2Socket.getOutputStream().write(clientMessage);
                
                //recebendo mensagem de node_2
                InputStream input = node2Socket.getInputStream();
                byte[] b = new byte[1024];
                input.read(b);
                String resposta = new String(b).trim();
                System.out.println("recebi resposta "+resposta);
                //se a resposta for igual à mensagem enviada, 
                //node_2 confirmou a existência do vendedodr 
                //e podemos cadastrar a Order
                if (resposta.equals(fullClientMessage)) {
                    Order o = new Order();
                    o.setIdPedido(messageParts[1]);
                    o.setSalesman(messageParts[2]);
                    o.setProduto(messageParts[3]);
                    o.setQuantity(Integer.parseInt(messageParts[4]));
                    saveOrder(o);
                    reply = fullClientMessage;  //devolve a mesma mensagem pq deu certo
                } else {
                    reply = resposta;           //devolve a mensagem de erro
                }
            }
            break;

            case "4": {
                //enviar mensagem para node_2 cadastrar o vendedor
                node2Socket = new Socket("localhost", 40998);
                node2Socket.getOutputStream().write(clientMessage);
                
                //recebe resposta de node_2
                InputStream input = node2Socket.getInputStream();
                byte[] b = new byte[1024];
                input.read(b);
                String resposta = new String(b).trim();
                
                //se a resposta for igual à mensagem enviada, 
                //node_2 cadastrou o vendedor e podemos cadastrar a Order agora
                if (resposta.equals(fullClientMessage)) {
                    Order o = new Order();
                    o.setIdPedido(messageParts[1]);
                    o.setSalesman(messageParts[2]);
                    o.setProduto(messageParts[5]);
                    o.setQuantity(Integer.parseInt(messageParts[6]));
                    saveOrder(o);
                    reply = fullClientMessage;  //devolve a mesma mensagem pq deu certo
                } else {
                    reply = resposta;           //devolve a mensagem de erro
                }                
            }
            break;
        }
        
        //manda resposta para o cliente
        clienteOut.write(reply.getBytes());
    }

    
    private void saveOrder(Order order) throws SQLException {
        OrderDbDao dao = new OrderDbDao();
        dao.save(order);
    }

}
