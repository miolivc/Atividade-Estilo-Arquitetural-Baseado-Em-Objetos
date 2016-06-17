/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package io.github.pod.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author wensttay
 */
public class Server {

    private ServerSocket serverSocket;

    public void turnOn() throws IOException {

        serverSocket = new ServerSocket();

        serverSocket.bind(new InetSocketAddress("localhost", 40998));

        startServerListenner();
    }

    ;
    
    public void turnOff() throws IOException {

        serverSocket.close();
    }

    public void restart() throws IOException {

        turnOff();
        turnOn();
    }

    private void startServerListenner() {

        while (!serverSocket.isClosed()) {

            try {

                Socket clientSocket = serverSocket.accept();

                new ServerThread(clientSocket, "Server Thread of the client "+clientSocket.getInetAddress().getHostAddress()).start();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

    }

}
