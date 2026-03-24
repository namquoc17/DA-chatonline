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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mrdai
 */
public class ServerThread extends Thread{
    String line=null;
    BufferedReader  is = null;
    PrintWriter os=null;
    Socket s=null;
    List<AccountInfo> listacc = null;

    ServerThread(Socket s,List<AccountInfo> list){
        this.s=s;
        this.listacc = list;
        
    }
    

    @Override
    public void run() {
    try{
        is= new BufferedReader(new InputStreamReader(s.getInputStream()));
        //os=new PrintWriter(s.getOutputStream());

    }catch(IOException e){
        System.out.println("IO error in server thread");
    }

    try {
        WriteServer write = null;
        while(true){
            line=is.readLine();
            //username:mess:rq
            String username = line.split(":")[0];
            String mess = line.split(":")[1];
            String rq = line.split(":")[2];
            if(rq.equals("info")){
                AccountInfo tmp = new AccountInfo(s,username);
                ServerHandle.addUser(tmp);
                write = new WriteServer(listacc);
                write.WriteUserOnline(username, os);
            } 
            if(rq.equals("ChatGroup")){
                
                write.WriteBroadcast(line,os);
            }
            if(rq.equals("Exit")){
                AccountInfo tmp = new AccountInfo(s,username);
                System.out.println(ServerHandle.accountinfolist.size());
                ServerHandle.removeUser(tmp);
                System.out.println(ServerHandle.accountinfolist.size());
                try{
                    System.out.println("listacc.size(): "+listacc.size());
                    /*for(int i=0; i<= listacc.size(); i++)
                    {
                        if(listacc.get(i).getUsername().equals(username)){
                            listacc.remove(i);
                            break;
                        }
                    }*/
                    System.out.println("listacc.size(): "+listacc.size());
                    write.setListacc(listacc);
                    write.WriteUserExit(username, os);
                }catch(Exception ex){
                    listacc.clear();
                    System.out.println("listacc.size(): "+listacc.size());
                }
               
                
            }
            if(rq.equals("ChatPrivate")){
                //format: username::ChatPrivate:desUser,mess
                String[]data = line.split(":");
                String sourceUser = data[0];
                String desUser = data[3].split(",")[0];
                String messfromsource = data[3].split(",")[1];
                write.WriteChatPrivate(sourceUser,desUser,messfromsource,os);
                System.out.println("_ChatPrivate:"+line);
            }
            if(rq.equals("ExitPrivate")){
                //format src::ExitPrivate:des
                
            }
            if(rq.equals("SendFile")){
                System.out.println("2_Server Receive SendFile from client");
                
                String[]data = line.split(":");
                String sourceUser = data[0];
                String desUser = data[3];
                
                try {
                    Thread.sleep(1000);
                    write.replySendFileOk(sourceUser, desUser, os);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            if(rq.equals("SendFileDone")){
                System.out.println("size accountlist :"+ServerTransferFile.getAccountinfolist().size());
                ServerTransferFile.removeallaccountinfolist();
                System.out.println("size accountlist :"+ServerTransferFile.getAccountinfolist().size());
            }
            System.out.println("Line from server: "+line);
            
        }   
    } catch (IOException e) {

        line=this.getName(); //reused String line for getting thread name
        System.out.println("IO Error/ Client "+line+" terminated abruptly");
    }
    catch(NullPointerException e){
        line=this.getName(); //reused String line for getting thread name
        System.out.println("Client "+line+" Closed");
        System.out.println("User Online: "+this.listacc.size());
        System.out.println("User Online ServerHandle: "+ServerHandle.accountinfolist.size());
    }
    finally{    
        try
        {
            
            if (is!=null){
                is.close(); 
                //System.out.println(" Socket Input Stream Closed");
            }

            if(os!=null){
                os.close();
                //System.out.println("Socket Out Closed");
            }
            if (s!=null){
                System.out.println("Disconnect "+s);
                s.close();
            }

        }
        catch(IOException ie){
            System.out.println("Socket Close Error");
        }
        }
    }
    public Thread getThreadByName(String threadName) {
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals(threadName)) return t;
        }
        return null;    
    }
    
}
