/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.PasswordStorageJavaApp;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.security.PublicKey;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.table.DefaultTableModel;
import org.bson.Document;

/**
 *
 * @author techtino
 */
public class ListAccountsGUI extends javax.swing.JFrame {

    /**
     * Creates new form ListAccountsGUI
     */
    
    MongoClientURI uri = new MongoClientURI("mongodb://Admin:mongopassword@34.89.31.98/?authSource=admin");
    MongoClient mongoClient = new MongoClient(uri);
    MongoDatabase database = mongoClient.getDatabase("Accounts");
    
    public ListAccountsGUI() {
        initComponents();
        greyButtons();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        RSA RSA = new RSA();
        AES aes=new AES("test1test1test12");
        String[] Columns = new String[]{"id","Username","Password","Website URL","Description"};
        DefaultTableModel AccountModel = new DefaultTableModel(Columns,0);
        MongoCollection<Document> collection = database.getCollection("test");
        try{
            for (Document cur : collection.find()) {
                System.out.println(cur.toJson());
                Object _id = cur.get("_id");
                Object Username = cur.get("Username");
                Object Passwordobj = cur.get("Password");
                Object WebsiteURL = cur.get("Website URL");
                Object Description = cur.get("Description");
                Object RSAKeyobj = cur.get("RSAPubKey");
                String RSAPubKeyString = RSAKeyobj.toString();

                //Get AES encrypted password and decrypt it
                String encryptedPassword = Passwordobj.toString();
                String RSAPassword = aes.decrypt(encryptedPassword);

                //Decrypt the RSA encrypted password
                PublicKey RSAPubKey = RSA.loadPublicKey(RSAPubKeyString);
                String decryptedRSA = RSA.decryptMessage(RSAPassword, RSAPubKey);

                AccountModel.addRow(new Object[] {_id,Username,decryptedRSA,WebsiteURL,Description});
            }
        }
        catch (Exception ex){}
        jTable1.setModel(AccountModel);
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Remove");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N
        jLabel1.setText("Accounts View");

        jButton3.setText("Change Master Account Password");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Edit");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 208, Short.MAX_VALUE)
                        .addComponent(jButton2))
                    .addComponent(jScrollPane1))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(201, 201, 201)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        Main Main = new Main();
        
        JPasswordField pwd = new JPasswordField(16);
        JOptionPane.showConfirmDialog(null, pwd,"Enter Password",JOptionPane.OK_CANCEL_OPTION);
        String password = pwd.getText();
        
        if (!"".equals(password) && password != null){
            Main.deleteMasterAccount();
            Main.createMasterAccount(password);
            JOptionPane.showMessageDialog(null,"Password successfully changed!");
        }
        else{
            JOptionPane.showMessageDialog(null,"Ok! Password will not be changed");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        UserDetailsGUI UserDetailsGUI= new UserDetailsGUI();
        UserDetailsGUI.main(new String [0]);
        setVisible(false);
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        try{
            int selectedRow = jTable1.getSelectedRow();
            int selectedColumn = 0;
            Object _idObj = jTable1.getValueAt(selectedRow, selectedColumn);
            String _id = _idObj.toString();
            Main Main = new Main();
            Main.deleteRegAccount(_id);     
            createJtable();
        }
        catch (Exception ex){
            JOptionPane.showMessageDialog(null,"You didn't select an account to delete (Select a row)");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        UserDetailsGUI UserDetailsGUI = new UserDetailsGUI();
        try{
            int selectedRow = jTable1.getSelectedRow();
            int selectedColumn = 0;
            Object _idObj = jTable1.getValueAt(selectedRow, selectedColumn);
            String _id = _idObj.toString();
            Main Main = new Main();
            String[] editableAccount = Main.findRegAccount(_id);

            UserDetailsGUI.setAccountToEdit(editableAccount);
            Main.deleteRegAccount(_id);
            UserDetailsGUI.main(new String [0]);
            setVisible(false);
            dispose();
            
        }
        catch (Exception ex){
            JOptionPane.showMessageDialog(null,"You didn't select an account to edit (Select a row)");
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    public void createJtable(){
        RSA RSA = new RSA();
        AES aes=new AES("test1test1test12");
        String[] Columns = new String[]{"id","Username","Password","Website URL","Description"};
        DefaultTableModel AccountModel = new DefaultTableModel(Columns,0);
        MongoCollection<Document> collection = database.getCollection("test");
        try{
            for (Document cur : collection.find()) {
                System.out.println(cur.toJson());
                Object _id = cur.get("_id");
                Object Username = cur.get("Username");
                Object Passwordobj = cur.get("Password");
                Object WebsiteURL = cur.get("Website URL");
                Object Description = cur.get("Description");
                Object RSAKeyobj = cur.get("RSAPubKey");
                String RSAPubKeyString = RSAKeyobj.toString();
                
                //Get AES encrypted password and decrypt it
                String encryptedPassword = Passwordobj.toString();
                String RSAPassword = aes.decrypt(encryptedPassword);
                
                //Decrypt the RSA encrypted password
                PublicKey RSAPubKey = RSA.loadPublicKey(RSAPubKeyString);
                String decryptedRSA = RSA.decryptMessage(RSAPassword, RSAPubKey);
        
                AccountModel.addRow(new Object[] {_id,Username,decryptedRSA,WebsiteURL,Description});
            }
        }
        catch (Exception ex){}
        jTable1.setModel(AccountModel);
        jScrollPane1.setViewportView(jTable1);
    }
    
    public void greyButtons(){
        UserLoginGUI UserLoginGUI = new UserLoginGUI();
        String accType= UserLoginGUI.getAccountType();
        if ("Regular".equals(accType)) {
            jButton3.setEnabled(false);
            jButton1.setEnabled(false);
            jButton2.setEnabled(false);
            jButton4.setEnabled(false);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ListAccountsGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListAccountsGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListAccountsGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListAccountsGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ListAccountsGUI().setVisible(true);
                
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    public javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
