/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatsocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author mrdai
 */
public class ServerHandle {
    static final int PORT = 4445;
    public static List<AccountInfo> accountinfolist;

    public List<AccountInfo> getAccountinfolist() {
        return accountinfolist;
    }

    public void setAccountinfolist(List<AccountInfo> accountinfolist) {
        this.accountinfolist = accountinfolist;
    }
    public static void addUser(AccountInfo item){
        accountinfolist.add(item);
    }
    public static void removeUser(AccountInfo item){
        try{
            for(int i=0; i<= accountinfolist.size(); i++){
                if(accountinfolist.get(i).getUsername().equals(item.getUsername())){
                    accountinfolist.remove(i);
                    break;
                }
            }
        }catch(Exception ex){
            accountinfolist.clear();
        }
        
    }
    public static void main(String args[]) {
        accountinfolist = new ArrayList<>();
        Socket s=null;
        ServerSocket ss2=null;
        ServerTransferFile transfer = new ServerTransferFile();
        Thread tTransfer = new Thread(transfer);
        tTransfer.setName("tTransferfile");
        tTransfer.start();
        System.out.println("Server Listening on port "+PORT+"......");
        try{
            ss2 = new ServerSocket(PORT); 

        }
        catch(IOException e){
            System.out.println("Server error");
        }

        while(true){
            try{
                synchronized(ss2) {
                    s= ss2.accept();
                    System.out.println("connection Established: Address "+ s);
                    ServerThread st=new ServerThread(s,ServerHandle.accountinfolist);
                    st.start();
                    
                }
            }

        catch(Exception e){
            //System.out.println("Connection Error");

        }
        }
    }
    
}
