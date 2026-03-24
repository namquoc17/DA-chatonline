/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatsocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mrdai
 */
public class WriteClient extends Thread{
    private static Socket socket;
    private static String username;
    private static String mess;
    public WriteClient(Socket socket,String username,String mess) {
        this.socket = socket;
        this.username = username;
        this.mess = mess;
    }
    public void run(){
        PrintWriter os=null;
        try {
            os= new PrintWriter(socket.getOutputStream());
            
            System.out.println("_WriteClient: "+username+":"+mess);
            
            System.out.println("_WriteClient: username: " +username );
            os.println(mess);
            os.flush();
        } catch (IOException ex) {
            System.out.println("err!");
            Logger.getLogger(WriteClient.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        }
            
    }
}
