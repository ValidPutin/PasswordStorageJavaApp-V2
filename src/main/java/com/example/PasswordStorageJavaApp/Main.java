package com.example.PasswordStorageJavaApp;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;
import org.bson.Document;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.bson.types.ObjectId;

@SpringBootApplication
public class Main {
    MongoClientURI uri = new MongoClientURI("mongodb://Admin:mongopassword@34.89.31.98/?authSource=admin");
    MongoClient mongoClient = new MongoClient(uri);
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
            findAccount.toJson();
            exists = true;
        }
        catch (Exception ex){}
        return exists;
    }

    public boolean checkPassCorrect(String Password,String hashedMasterPassword) {
        boolean correct = false;
        if(BCrypt.checkpw(Password, hashedMasterPassword)) {
            correct = true;
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
        RSA RSA = new RSA();
        AES aes=new AES("test1test1test12"); // creates aes with key: test1test1test12
        MongoCollection<Document> collection = database.getCollection("test");
        try{
            
            //Encrypt message with RSA and prepare for storing
            Map<String, Object> keys = RSA.getRSAKeys();
            PrivateKey privateKey = (PrivateKey) keys.get("private");
            PublicKey publicKey = (PublicKey) keys.get("public");
            String RSAPassword = RSA.encryptMessage(Password,privateKey);
            String pubkeystring = RSA.convertPublicKeyToString(publicKey);
            
            //Encrypt RSA password with AES and prepare to store
            String AESPassword = aes.encrypt(RSAPassword);

            Document document = new Document("Username",Username).append("Password",AESPassword).append("RSAPubKey",pubkeystring).append("Website URL",WebsiteURL).append("Description",Description);        
            collection.insertOne(document);
        }
        catch(Exception EX){}


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
        AES aes=new AES("test1test1test12");
        try{
            for (Document cur : collection.find()) {
                Object Passwordobj = cur.get("Password");
                Object RSAKeyobj = cur.get("RSAPubKey");
                String RSAPubKeyString = RSAKeyobj.toString();
                
                //Get AES encrypted password and decrypt it
                String encryptedPassword = Passwordobj.toString();
                String RSAPassword = aes.decrypt(encryptedPassword);
                
                //Decrypt the RSA encrypted password
                PublicKey RSAPubKey = RSA.loadPublicKey(RSAPubKeyString);
                String decryptedRSA = RSA.decryptMessage(RSAPassword, RSAPubKey);
                passwordList[passwordListIndex] = decryptedRSA;
                passwordListIndex++;
            }
        }
        catch (Exception ex){}

        return passwordList;
    }

    public String[] findRegAccount(String _id){
        String[] account = new String[4];
        MongoCollection<Document> collection = database.getCollection("test");
        for (Document cur : collection.find()) {
            System.out.println("hi");
            String ID = cur.getObjectId("_id").toString();
            if (ID.equals(_id)){
                Object Usernameobj = cur.get("Username");
                Object Passwordobj = cur.get("Password");
                Object Websiteobj = cur.get("Website URL");
                Object Descriptionobj = cur.get("Description");
                account[0] = Usernameobj.toString();
                account[1] = Passwordobj.toString();
                account[2] = Websiteobj.toString();
                account[3] = Descriptionobj.toString();
            }
        }
        return account;
    }

    public boolean checkRegPasswordCorrect(String passwordEnc){
        boolean correct = false;
        String [] storedRegPasswords = getRegPasswords();
        for (int i = 0; i <= storedRegPasswords.length-1;i++){
            if (passwordEnc.equals(storedRegPasswords[i])){
                correct = true;
            }
        }
        return correct;
    }
}
