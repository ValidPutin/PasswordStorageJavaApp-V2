package com.example.PasswordStorageJavaApp;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoDatabase adminDatabase = mongoClient.getDatabase("admin");
	public static void main(String[] args) {
                    UserLoginGUI UserLoginGUI = new UserLoginGUI();
                    UserLoginGUI.main(new String[0]);
	}
        
        public boolean checkMasterAccountExists(){
        MongoCollection<Document> collection = adminDatabase.getCollection("MasterAccount");
        boolean exists = false;
        try{
            Document findAccount = collection.find().first();
            System.out.println(findAccount.toJson());
            exists = true;
        }
        catch (Exception ex){
            System.out.println("Master account does not exist!");
            exists = false;
        }
        return exists;
    }
    
    public void createMasterAccount(String password){
        //Insert hashing function here, store hashed password instead of 'hello'
        
        String hashedPassword = "hashed" + password;
        MongoCollection<Document> collection = adminDatabase.getCollection("MasterAccount");
        Document document = new Document("password",hashedPassword);
        collection.insertOne(document);
    }
    
    public void deleteMasterAccount(){
        MongoCollection<Document> collection = adminDatabase.getCollection("MasterAccount");
        Document MasterAcc = collection.find().first();
        collection.deleteOne(MasterAcc);
    }
    
    public String getMasterPassword(){
        MongoCollection<Document> collection = adminDatabase.getCollection("MasterAccount");
        Document findPassword = collection.find().first();
        String storedPassword = (String) findPassword.get("password");
        return storedPassword;
    }
    
    public void addRegAccount(String Username, String Password, String WebsiteURL, String Description){
        MongoCollection<Document> collection = database.getCollection("test");
        
        String EncryptedPassword = Password + "encrypted"; //This is where you add the encryption mechanism, we store encrypted passwords
        Document document = new Document("Username",Username).append("Password",EncryptedPassword).append("Website URL",WebsiteURL).append("Description",Description);
        
        collection.insertOne(document);
    }
    
    public String[] getRegPasswords(){

        MongoCollection<Document> collection = database.getCollection("test");
        int passwordListIndex = 0;
        String[] passwordList = new String[100];
        for (Document cur : collection.find()) {
            Object Passwordobj = cur.get("Password");
            String encryptedPassword = Passwordobj.toString();
            String decryptedPassword = encryptedPassword; //replace this with decryption function so array stores decrypted version
            System.out.println(decryptedPassword);
            passwordList[passwordListIndex] = decryptedPassword;
        }
        return passwordList;
        
    }
     public void deleteRegAccount(){
         MongoCollection<Document> collection = database.getCollection("test");
         
         
         
    }
        

}
