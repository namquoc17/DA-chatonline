/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatsocket;

import java.io.Serializable;

/**
 *
 * @author MrDai
 */
public class Data_file implements Serializable{
    public byte[] file;
    public String source_name;
    public String des_name;
    public String filename;
    
    public byte[] getFile(){
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
     public String getsource_name() {
        return source_name;
    }

    public void setsource_name(String name) {
        this.source_name = name;
    }
      public String getfilename() {
        return filename;
    }

    public void setfilename(String filename) {
        this.filename = filename;
    }
     public String getdes_name() {
        return des_name;
    }

    public void setdes_name(String name) {
        this.des_name = name;
    }


    @Override
    public String toString() {
       return filename;
    }

  
}
