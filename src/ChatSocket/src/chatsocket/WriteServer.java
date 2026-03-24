/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatsocket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mrdai
 */
public class WriteServer {
    //private Socket socket;
    private static List<AccountInfo> listacc;
   
    
    public WriteServer(List<AccountInfo> listacc) {
        //this.socket = socket;
        this.listacc = listacc;
  
    }

    public List<AccountInfo> getListacc() {
        return listacc;
    }

    public void setListacc(List<AccountInfo> listacc) {
        this.listacc = listacc;
    }
    public void WriteChatPrivate(String sourceUser,String desUser,String messfromsource,PrintWriter os){
        String mess = sourceUser+":"+messfromsource+":ChatPrivate:"+desUser;
        System.out.println("_WriteChatPrivate: "+sourceUser+":"+messfromsource+":ChatPrivate:"+desUser);
        for(AccountInfo item:listacc){
            if(item.getUsername().equals(sourceUser) || item.getUsername().equals(desUser))
            {
                try {
                    os = new PrintWriter(item.getSocket().getOutputStream());
                    os.println(mess);
                    os.flush();
                } catch (IOException ex) {
                    Logger.getLogger(WriteServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
       
    }
    public void WriteUserOnline(String username,PrintWriter os){
        String mess = "Server: "+username+" is online!:UserOnline:";
        String mess2 = ConvertAccToString();
        WriteBroadcast(mess+";"+mess2,os);
    }
    public void WriteUserExit(String username,PrintWriter os){
        String mess = "Server: "+username+" logged out!:UserExit:";
        String mess2 = ConvertAccToString();
        WriteBroadcast(mess+";"+mess2,os);
    }
    public void WriteUserPrivateExit(String mess,PrintWriter os){
        //format src::ExitPrivate:des
        String src = mess.split(":")[0];
        for(AccountInfo item:listacc){
            if(item.getUsername().equals(src))
            {
                try {
                    os = new PrintWriter(item.getSocket().getOutputStream());
                    os.println(mess);
                    os.flush();
                } catch (IOException ex) {
                    Logger.getLogger(WriteServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    public String ConvertAccToString(){
        String result = "ListUserOnline:";
        for(AccountInfo item:listacc){
            result+=item.getUsername()+",";
        }
        return result;
    }
    public void WriteBroadcast(String mess,PrintWriter os){
        System.out.println("WriteBroadcast listacc.size(): "+listacc.size());
        if(listacc!=null){
            for(AccountInfo item:listacc){
            try {
                os = new PrintWriter(item.getSocket().getOutputStream());
                os.println(mess);
                os.flush();
            } catch (IOException ex) {
                Logger.getLogger(WriteServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
        }else{
            System.out.println("Nobody online!");
        }
        
    }
    public void replySendFileOk(String src, String des,PrintWriter os){
        System.out.println("_Reply send file from server: Ok");
        System.out.println(" --time Reply send file from server:: "+new Date().getTime() );
        String messreply = src+":Ok:SendFile:"+des;
        System.out.println("--messreply: "+messreply);
        for(AccountInfo item:listacc){
            if(item.getUsername().equals(src))
            {
                
                try {
                    os = new PrintWriter(item.getSocket().getOutputStream());
                    os.println(messreply);
                    os.flush();
                } catch (IOException ex) {
                    Logger.getLogger(WriteServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        for(AccountInfo item:listacc){
            if(item.getUsername().equals(des))
            {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(WriteServer.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    os = new PrintWriter(item.getSocket().getOutputStream());
                    os.println(messreply);
                    os.flush();
                } catch (IOException ex) {
                    Logger.getLogger(WriteServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
         
    }
    public void WriteSendFile(String src,String des,String mess,PrintWriter os){
        
        System.out.println("_Server WriteSendFile to Client: src: "+src+" des: "+des+" mess: "+mess);
        for(AccountInfo item:listacc){
            if(item.getUsername().equals(src) || item.getUsername().equals(des))
            {
                try {
                    os = new PrintWriter(item.getSocket().getOutputStream());
                    os.println(mess);
                    os.flush();
                } catch (IOException ex) {
                    Logger.getLogger(WriteServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
    }
    public static void ReadWriteFile(String src){
        System.out.println("3_Server Read_FileServer");
        Socket client = null;
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        for(AccountInfo item : listacc){
            if(item.getUsername().equalsIgnoreCase(src)){
                        client = item.getSocket();
                        break;
            }
        }
        try {
            
            ois = new ObjectInputStream(client.getInputStream());
            try {
                Data_file datafile = (Data_file)ois.readObject();
                System.out.println("3_Server Read_FileServer: read file successfully");
                for(AccountInfo item : listacc){
                    if(item.getUsername().equalsIgnoreCase(datafile.getdes_name())){
                        System.out.println("---Socket recieve: "+item.getSocket().toString());
                        oos = new ObjectOutputStream(item.getSocket().getOutputStream());
                        oos.writeObject(datafile);
                        System.out.println("4_Server Send_FileServer: "+datafile.toString());
                    }
                }
                //Read_ChatServer read =new Read_ChatServer(client);
                //read.start();
            } catch (ClassNotFoundException ex) {
               
            }
            
        } catch (IOException ex) {
            try {
                ois.close();
                oos.close();
            } catch (IOException ex1) {
            }
        }
    }
}
