/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.PasswordStorageJavaApp;

import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher; //creating cipher txt
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AES {
    
    private static final String aes1="AES" ;
    private byte []keyValue; //key array
    
    //constructer aes 
    public AES(String key){ 
        keyValue=key.getBytes();//string key converted to bytes and stored as keyValue
    }  
    //encrypting data method
    public String encrypt (String Data) throws Exception{ //encryps the string data
        Key key=generatedKey(); //step 1 create key
        Cipher c=Cipher.getInstance(aes1);
        c.init(Cipher.ENCRYPT_MODE,key); //cipher encyptes with key
        byte[] encVal=c.doFinal(Data.getBytes()); //encrype values
        String encryptedValue=Base64.getEncoder().encodeToString(encVal);//encode
        return encryptedValue;
    }
    
    //decrypting data method
    public String decrypt (String encryptedData)throws Exception { 
        Key key=generatedKey();
        Cipher c=Cipher.getInstance(aes1);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = Base64.getDecoder().decode(encryptedData);//decode
        byte[] decValue=c.doFinal(decordedValue);
        String decryptedValue=new String(decValue);
        return decryptedValue;
    }
    
    //key method
    private  Key generatedKey() throws Exception { //generates the key
        Key key=new SecretKeySpec (keyValue, aes1); 
        return key;
    } 
 
 
    public static void main(String args[]) {
        try {
            //AES class obj
            AES aes=new AES("test1test1test12"); //key
            String encdata=aes.encrypt("test2"); //data for encryption
            System.out.println("encrypted data: "+encdata);
            String decdata=aes.decrypt(encdata);
            System.out.println("decrypted data: "+ decdata);
        }catch (Exception ex){
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
