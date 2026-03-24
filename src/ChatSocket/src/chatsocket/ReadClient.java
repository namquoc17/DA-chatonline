/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatsocket;

import static chatsocket.ClientHandle.PORT;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mrdai
 */
public class ReadClient extends Thread{
    private static Socket socket;
    public static String mess;
    public ReadClient(Socket socket) {
        this.socket = socket;
    }

    public static String getMess() {
        return mess;
    }

    public static void setMess(String mess) {
        ReadClient.mess = mess;
    }
    
    @Override
    public void run() {
        
        
        BufferedReader is=null;
        try {
            is=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (IOException e){
            e.printStackTrace();
            System.err.print("IO Exception");
        }
        try{
            while(true){
                mess = is.readLine();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        System.out.println("Socket read Error");
        }
        finally{

            try {
                is.close();
                socket.close();
                System.out.println("Connection Closed");
            } catch (IOException ex) {
                Logger.getLogger(ReadClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            

        }
    }
}
