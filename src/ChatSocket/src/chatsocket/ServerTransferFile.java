/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatsocket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mrdai
 */
public class ServerTransferFile implements Runnable{
    static final int PORT = 5050;
    public static List<Socket> accountinfolist;
    //private String src=null;
    static Socket s=null;
    static ServerSocket ss2=null;
    static boolean flag = true;
    public ServerTransferFile() {
        accountinfolist = new ArrayList<>();
    }
    /*public ServerTransferFile() {
        this.src = src;
        accountinfolist = new ArrayList<>();
    }*/

    public static List<Socket> getAccountinfolist() {
        return accountinfolist;
    }

    public static void setAccountinfolist(List<Socket> accountinfolist) {
        ServerTransferFile.accountinfolist = accountinfolist;
    }

    public static void removeallaccountinfolist(){
       accountinfolist.clear();
    }
    
    @Override
    public void run() {
        //System.out.println(Thread.currentThread().getName());
        System.out.println("ServerTransferFile Listening on port "+PORT+"......");
        //System.out.println(" --time ServerTransferFile: "+new Date().getTime() );
        try{
            ss2 = new ServerSocket(PORT);

        }
        catch(IOException e){
            System.out.println("Server error");
        }
        
        while(this.flag){
            try{
                synchronized(ss2){
                    s= ss2.accept();
                    System.out.println("---ServerTransferFile connection Established: Address "+ s);
                    accountinfolist.add(s);
                    //System.out.println("--- add accountinfolist ok dize: "+accountinfolist.size());
                    if(accountinfolist.size()>=2){
                         ReadFile();
                    }
                }
                
            }

            catch(IOException e){
                System.out.println("Connection Error");

            }
        }
    }
    
    public void closeServerTransferFile(){
        try {
            this.flag= false;
            
            System.out.println("Close connect Transferfile to socket: "+this.ss2);
            this.ss2.close();
            
            
        } catch (IOException ex) {
            Logger.getLogger(ServerTransferFile.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Err closeServerTransferFile");
        }
       
    }
    public Thread getThreadByName(String threadName) {
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals(threadName)) return t;
        }
        return null;    
    }
    public void ReadFile(){
        System.out.println("---ReadFile");
        Socket socketsrc = null;
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        /*
        for(AccountInfo item: accountinfolist){
            if(item.getUsername().equalsIgnoreCase(src)){
                socketsrc = item.getSocket();
                System.out.println("src: "+src+ " item.usr: "+item.getUsername());
            }
        }*/
        socketsrc = accountinfolist.get(0);
        try {
            System.out.println("---Socket send: "+socketsrc.toString());
            ois = new ObjectInputStream(socketsrc.getInputStream());
            try {
                Data_file datafile = (Data_file)ois.readObject();
                System.out.println("---Server Read_FileServer: read file successfully");
                for(Socket item : accountinfolist){
                    
                    System.out.println("---Socket recieve: "+item.toString());
                    oos = new ObjectOutputStream(item.getOutputStream());
                    oos.writeObject(datafile);
                    System.out.println("---Server Read_FileServer: "+datafile.toString());
                    
                }
               
            } catch (ClassNotFoundException ex) {
                
            }
            
        } catch (IOException ex) {
            try {
                if(ois!=null){
                    ois.close();
                }
                if(oos!=null){
                    oos.close();
                }
                
                
            } catch (IOException ex1) {
                
            }
        }
    }
    
    
}
