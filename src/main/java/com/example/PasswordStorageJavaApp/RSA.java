/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.PasswordStorageJavaApp;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
 
//java simple algorithm for encrypting and decrypting
public class RSA {
 
    public static void main(String[] args) throws Exception {
        System.out.println("Enter Your Password");
        Scanner password = new Scanner(System.in);  // Create a Scanner object
        String pass = password.nextLine (); //user input for credentials 
        String Credentials = pass; 
 
        // Generate public and private keys using RSA
        Map<String, Object> keys = getRSAKeys();
 
        PrivateKey privateKey = (PrivateKey) keys.get("private");
        PublicKey publicKey = (PublicKey) keys.get("public");
 
        String EncryptedText = encryptMessage(Credentials, privateKey);
        String DecryptedText = decryptMessage(EncryptedText, publicKey);
 
        System.out.println("User Input --->:" + Credentials); //user will enter their password in this field
        System.out.println("encrypted Data:" + EncryptedText); //The encryption algorithm will generate this text
        System.out.println("decrypted Data:" + DecryptedText); //User will be given back original input
 
    }
 
    // Obtain RSA keys for public and private key 
    public static Map<String,Object> getRSAKeys() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); //This value can be changed to either 2048 or 1024 depending on if the user wants to prioritise performance or security
        KeyPair KP = keyPairGenerator.generateKeyPair();
        PrivateKey PRIVKEY = KP.getPrivate();
        PublicKey PUBKEY = KP.getPublic();
 
        Map<String, Object> Genkeys = new HashMap<String,Object>();
        Genkeys.put("private", PRIVKEY); //these are the generated keys one is for the public key (kept on client) other is kept on server the private key
        Genkeys.put("public", PUBKEY);
        return Genkeys;
    }
    
    
    public static PublicKey loadPublicKey(String stored) throws GeneralSecurityException, IOException 
    {
        byte[] data = Base64.getDecoder().decode((stored.getBytes()));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        return fact.generatePublic(spec);

   }
    
    
    public String convertPublicKeyToString(PublicKey public_key){
        //converting public key to byte            
        byte[] byte_pubkey = public_key.getEncoded();
        System.out.println("\nBYTE KEY::: " + byte_pubkey);
        //converting byte to String 
        String str_key = Base64.getEncoder().encodeToString(byte_pubkey);
        // String str_key = new String(byte_pubkey,Charset.);
        System.out.println("\nSTRING KEY::" + str_key);
        return str_key;
    }
    
 
    // Encrypt using RSA private key
    public static String encryptMessage(String plainText, PrivateKey publicKey) throws Exception {
        Cipher CI_EN = Cipher.getInstance("RSA");
        CI_EN.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.getEncoder().encodeToString(CI_EN.doFinal(plainText.getBytes()));
    }
    
    // Decrypt the data by using the generated private key.
    public static String decryptMessage(String encryptedText, PublicKey privateKey) throws Exception {
        Cipher CI_DE = Cipher.getInstance("RSA");
        CI_DE.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(CI_DE.doFinal(Base64.getDecoder().decode(encryptedText)));
    }
}
