package com.example.PasswordStorageJavaApp;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.bson.types.ObjectId;

@SpringBootApplication
public class Main {

        MongoClient mongoClient = new MongoClient( "34.89.31.98" );
        MongoDatabase database = mongoClient.getDatabase("Accounts");
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
        
        public boolean checkPassCorrect(String Password,String hashedMasterPassword) {
            boolean correct = false;
            if(BCrypt.checkpw(Password, hashedMasterPassword)) {
                correct = true;
                System.out.println("Password Match");
                System.out.println("Salt: " + BCrypt.gensalt(10) + " Hash: " + hashedMasterPassword);
            }
            else{correct = false;}
            return correct;
        }
        
        public void createMasterAccount(String password){
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
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
        
        public void deleteRegAccount(String _id){
               MongoCollection<Document> collection = database.getCollection("test");
               Document document = new Document("_id", new ObjectId (_id));
               collection.deleteOne(document);
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
}
