/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatsocket;

import javax.swing.JFrame;

/**
 *
 * @author mrdai
 */
public class ChatPrivateClass {
    private ChatPrivate chatprivate;
    private String source;
    private String des;
    public ChatPrivateClass(){
        
    }

    public ChatPrivateClass(ChatPrivate chatprivate, String source, String des) {
        this.chatprivate = chatprivate;
        this.source = source;
        this.des = des;
    }

    public ChatPrivate getChatprivate() {
        return chatprivate;
    }

    public void setChatprivate(ChatPrivate chatprivate) {
        this.chatprivate = chatprivate;
    }
   

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
    
}
