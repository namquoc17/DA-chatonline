/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatsocket;

import static chatsocket.ClientHandle.address;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mrdai
 */
public class ClientTransferFile {
    static final int PORT = 5050;
    private static String src=null;
    private static String des = null;
    private  static String pathfile = null;
    static InetAddress address;
    static Socket s=null;

    public ClientTransferFile() {
        
        System.out.println("--new ClientTransferFile ok");
         System.out.println(" --time "+new Date().getTime() );
    }

    public static Socket getS() {
        return s;
    }

    public static void setS(Socket s) {
        ClientTransferFile.s = s;
    }
    
    /*
    @Override
    public void run() {
        try {
            address=InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(ClientTransferFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            s=new Socket(address, PORT);
            WriteFileClient write = new WriteFileClient(s,src,des,pathfile);
            write.start();
            ReadClient read = new ReadClient(s);
            read.start();
            
        }
        catch (IOException e){
            e.printStackTrace();
            System.err.print("IO Exception");
        }
    }*/
    public static void Init(String src,String des,String pathfile) {
         System.out.println(" --time init: "+new Date().getTime() );
        ClientTransferFile.src = src;
        ClientTransferFile.des = des;
        ClientTransferFile.pathfile = pathfile;
        System.out.println("--src : "+src+" des: "+des+" pathfile: "+pathfile);
        try {
            address=InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(ClientTransferFile.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("--Err Init InetAddress.getLocalHost()");
        }
        System.out.println("address: "+address);
        try { 
            s=new Socket(address, PORT);
        } catch (IOException ex) {
            Logger.getLogger(ClientTransferFile.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("--Err new Socket(address, PORT);");
        }
        System.out.println("--Init new port to transfer file: "+s);
    }
    public void WriteFileClient(){
        System.out.println("_WriteFileClient from Client socket : "+s);
        System.out.println(" --time: "+new Date().getTime() );
        Data_file data = createDatafile(this.src,this.des,this.pathfile);
        System.out.println("--Filename :"+data.getfilename());
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(s.getOutputStream());
            System.out.println("--Filename :"+data.getfilename());
            oos.writeObject(data);
            System.out.println("--Write_SendFiles: pass send file");
        } catch (IOException ex) {
            try {
                oos.close();
            } catch (IOException ex1) {
                 System.out.println("-Write_SendFiles Err:"+ex.getMessage());
                
            }
              System.out.println("-Write_SendFiles Err:"+ex.getMessage());
        }
    }
    public Data_file createDatafile(String src,String des,String pathfile){
        System.out.println("--createDatafile");
        System.out.println("---src : "+src+" des: "+des+" pathfile: "+pathfile);
        File f = new File(pathfile);
        Data_file datafile = null;
        FileInputStream fr;
        try {
            fr = new FileInputStream(f);
            byte[] b = new byte[fr.available()];
            fr.read(b);
            datafile = new Data_file();
            datafile.setFile(b);
            datafile.setdes_name(des);
            datafile.setsource_name(src);
            datafile.setfilename(f.getName());
            System.out.println("---bytes: "+Arrays.toString(b));
        } catch (FileNotFoundException ex) 
        {
        } catch (IOException ex) {
        }
        return datafile;
    }
    public void closeTransferFileFromClient(){
        try {
            System.out.println("Close on port "+s);
            s.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientTransferFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 }