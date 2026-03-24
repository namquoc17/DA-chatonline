/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatsocket;

/**
 *
 * @author mrdai
 */
public class MainClass {
    public static void main(String args[]){
        //LoginForm loginf = new LoginForm();
        //loginf.setVisible(true);
        
        Thread th = new Thread(){
            public void run(){
                LoginForm loginf = new LoginForm();
                loginf.setVisible(true);
            }
        };
        th.start();
        
    }
}
