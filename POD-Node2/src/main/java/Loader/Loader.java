/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Loader;

import io.github.pod.server.Server;
import java.io.IOException;

/**
 *
 * @author wensttay
 */
public class Loader
{

    public static void main(String[] args) throws IOException
    {
        Server s = new Server();
        
        s.turnOn();
    }

}
