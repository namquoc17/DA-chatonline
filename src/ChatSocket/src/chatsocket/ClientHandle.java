/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatsocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author mrdai
 */
public class ClientHandle {
    static final int PORT = 4445;
    private static String user ;
    static InetAddress address;
    static Socket socket=null;
    static String line=null;
    static BufferedReader br=null;
    static BufferedReader is=null;
    static PrintWriter os=null;
    private static String pathfile = null;
    
    public ClientHandle() {
    }

    public static Socket getSocket() {
        return socket;
    }

    public static void setSocket(Socket socket) {
        ClientHandle.socket = socket;
    }

    public static String getPathfile() {
        return pathfile;
    }

    public static void setPathfile(String pathfile) {
        ClientHandle.pathfile = pathfile;
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        ClientHandle.user = user;
    }
    
    /*
    public static void main(String args[]) throws IOException{
        
        address=InetAddress.getLocalHost();
        
        try {
            s=new Socket(address, PORT);
            sendInfo(s);
            WriteClient write = new WriteClient(s,user);
            write.start();
            ReadClient read = new ReadClient(s);
            read.start();
            
        }
        catch (IOException e){
            e.printStackTrace();
            System.err.print("IO Exception");
        }
    }*/
    
    public static void sendInfo(String username){
        
        //System.out.println(username);
        //System.out.println("_sendinfo");
        try {
            os= new PrintWriter(socket.getOutputStream());
            os.println(username+":info:info");
            System.out.println("_sendInfo: "+username+":info");
            os.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void InitClient(String username){
        
        try {
           //System.out.println("_InitClient");
           //System.out.println("0:"+username);
           user = username;
           address=InetAddress.getLocalHost();
           System.out.println("address: "+address);
           socket=new Socket(address, 4445); // You can use static final constant PORT_NUM
           br= new BufferedReader(new InputStreamReader(System.in));
           is=new BufferedReader(new InputStreamReader(socket.getInputStream()));
           os= new PrintWriter(socket.getOutputStream());
           //System.out.println("1:"+username);
           sendInfo(username);
           //System.out.println("2:"+username);
       }
       catch (IOException e){
           e.printStackTrace();
           System.err.print("IO Exception");
       }
    }
    public static void CloseClient(){
        try {
            System.out.println("Connection Closed: "+socket);
            is.close();
            os.close();
            br.close();
            socket.close();
            
        } catch (IOException ex) {
            Logger.getLogger(ClientHandle.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("CloseClient err!");
        }
    }
    public void WriteExitClient(){
        try {
            os= new PrintWriter(socket.getOutputStream());
            os.println(user+"::Exit");
            System.out.println("_WriteExitClient: "+user+"::Exit");
            os.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void WriteExitPrivateClient(String des){
        try {
            os= new PrintWriter(socket.getOutputStream());
            os.println(user+"::ExitPrivate:"+des);
            System.out.println("WriteExitPrivateClient: "+user+"::ExitPrivate:"+des);
            os.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static String ReadClient(){
        String mess;
        ReadClient read = new ReadClient(socket);
        read.start();
        mess = read.getMess();
        return mess;
    }
    public static void WriteClient(String mess){
        WriteClient write = new WriteClient(socket,user,mess);
        write.start();
    }
    public void WriteChatPrivate(String desUser,String mess){
        try {
            //format: username::ChatPrivate:desUser,mess
            os= new PrintWriter(socket.getOutputStream());
            os.println(user+"::ChatPrivate:"+desUser+","+mess);
            System.out.println("_WriteChatPrivate_Client "+user+"::ChatPrivate:"+desUser+","+mess);
            os.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void WriteAlertSendFile(String des){
        try {
            //format: username::SendFile:desUser,mess
            os= new PrintWriter(socket.getOutputStream());
            os.println(user+"::SendFile:"+des);
            System.out.println("1_WriteAlertSendFile:"+user+"::SendFile:"+des);
            os.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void Write_sendfile(String mess){
        //format: src::SendFile:des:filename:File
         try {
            os= new PrintWriter(socket.getOutputStream());
            os.println(mess);
            System.out.println("_Client Write_sendfile:"+mess);
            os.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void Write_SendFiles(Data_file data){
        System.out.println( " 5 - Write_SendFiles"+new Date().getTime());
        System.out.println("---Socket send file: "+socket.toString());
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("-Write_SendFiles:"+data.getfilename());
            oos.writeObject(user+":"+data);
            System.out.println("-Write_SendFiles: pass send file");
        } catch (IOException ex) {
            try {
                oos.close();
            } catch (IOException ex1) {
                 System.out.println("-Write_SendFiles Err:"+ex.getMessage());
                
            }
              System.out.println("-Write_SendFiles Err:"+ex.getMessage());
        }
    }
    public void closeTransferFileMess(){
        try {
            //format: username::SendFile:desUser,mess
            os= new PrintWriter(socket.getOutputStream());
            os.println(user+"::SendFileDone");
            System.out.println("--closeTransferFileMess:"+user+"::SendFileDone");
            os.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandle.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
}
