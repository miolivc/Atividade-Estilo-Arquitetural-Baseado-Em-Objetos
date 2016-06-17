/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Loader;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Gamer
 */
public class LoaderTest {
    
    public static void main(String[] args) throws IOException {
        
        Socket s = new Socket("localhost", 40997);
        
        String msg = "4:PD002:3:João Pedro:83 9 8876 0909:12:20";
        
        s.getOutputStream().write(msg.getBytes());
        
        byte[] b = new byte[1024];
        
        s.getInputStream().read(b);
        
        System.out.println(new String(b));
        
    }
    
}
