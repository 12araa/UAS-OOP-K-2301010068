/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package uas.oop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import static uas.oop.dbkoneksi.koneksi;

/**
 *
 * @author R
 * 
 */
public class formInventory extends javax.swing.JFrame {

//    String edproduk= "";
    DefaultTableModel TM = new DefaultTableModel();
    private String mode = "awal";
    /**
     * Creates new form formInventory
     * @throws java.sql.SQLException
     */
    public formInventory() throws SQLException {
        initComponents();
        aturMode("awal");
        cbkategori.removeAllItems();
       
        
        TM.addColumn("ID"); 
        TM.addColumn("Kode");
        TM.addColumn("Nama Produk");
        TM.addColumn("Kategori");
        TM.addColumn("Deskripsi");
        TM.addColumn("Harga");
        TM.addColumn("Stok");
        DTM.setModel(TM);
        DTM.getColumnModel().getColumn(0).setMinWidth(0);
        DTM.getColumnModel().getColumn(0).setMaxWidth(0);
        DTM.getColumnModel().getColumn(0).setWidth(0);
        
        loadKategori(); 
        this.dtProdukList();
        fieldInputEnabled(false);
        
    }
    
    private void aturMode(String kondisi) {
        mode = kondisi;
        

        switch(kondisi) {
            case "awal":
                fieldInputEnabled(false);
                resetForm();

                btnTambah.setText("Tambah");
                btnTutup.setText("Tutup");
                btnUpdate.setText("Edit");

                btnTambah.setEnabled(true);
                btnUpdate.setEnabled(false);
                btnHapus.setEnabled(false);
                btnTutup.setEnabled(true);
                break;

            case "tambah":
                fieldInputEnabled(true);
                resetForm();

                btnTambah.setText("Simpan");
                btnTutup.setText("Batal");

                btnTambah.setEnabled(true);
                btnUpdate.setEnabled(false);
                btnHapus.setEnabled(false);
                btnTutup.setEnabled(true);
                break;

            case "pilih":
                fieldInputEnabled(false);

                btnTambah.setEnabled(false);
                btnUpdate.setEnabled(true);
                btnHapus.setEnabled(true);
                btnTutup.setText("Batal");
                break;

            case "edit":
                fieldInputEnabled(true);

                btnUpdate.setText("Simpan");
                btnTutup.setText("Batal");

                btnUpdate.setEnabled(true);
                btnTambah.setEnabled(false);
                btnHapus.setEnabled(false);
                btnTutup.setEnabled(true);
                break;
        }
    }
    
    private int idProdukTerpilih = -1; 
    
    private void fieldInputEnabled(boolean status) {
        txkode.setEditable(status);
        txnama.setEditable(status);
        txdesk.setEditable(status);
        txharga.setEditable(status);
        txstok.setEditable(status);
        cbkategori.setEnabled(status);
    }
    
    private void tambahData() throws SQLException{
        String kode = txkode.getText();
        String nama = txnama.getText();
        String deskripsi = txdesk.getText();
        double harga = Double.parseDouble(txharga.getText());
        int stok = Integer.parseInt(txstok.getText());
        String kategori = (String) cbkategori.getSelectedItem();

        int id_kategori = getKategoriId(kategori);
        
        Connection cnn = koneksi();
        PreparedStatement PS = cnn.prepareStatement("INSERT INTO item(kode, nama, deskripsi, harga, stok, id_kategori) VALUE (?,?,?,?,?,?);");
        PS.setString(1, kode);
        PS.setString(2, nama);
        PS.setString(3, deskripsi);
        PS.setDouble(4, harga);
        PS.setInt(5, stok);
        PS.setInt(6, id_kategori);
        PS.executeUpdate();
    }
    
    private void updateData() throws SQLException {
        String kode = txkode.getText();
        String nama = txnama.getText();
        String deskripsi = txdesk.getText();
        String harga = txharga.getText();
        String stok = txstok.getText();
        String kategori = (String) cbkategori.getSelectedItem();
        int id_kategori = getKategoriId(kategori);

        Connection cnn = koneksi();
        PreparedStatement PS = cnn.prepareStatement(
            "UPDATE item SET nama=?, deskripsi=?, harga=?, stok=?, id_kategori=? WHERE id=?"
        );
        PS.setString(1, nama);
        PS.setString(2, deskripsi);
        PS.setString(3, harga);
        PS.setString(4, stok);
        PS.setInt(5, id_kategori);
        PS.setInt(6, idProdukTerpilih);
        PS.executeUpdate();
    }

    private void destroyData() throws SQLException {
        String kode = txkode.getText();

        Connection cnn = koneksi();
        PreparedStatement PS = cnn.prepareStatement("DELETE FROM item WHERE kode=?");
        PS.setString(1, kode);
        PS.executeUpdate();
    }

    private void resetForm() {
        txnama.setText("");
        txkode.setText("");
        cbkategori.setSelectedIndex(0);
        txstok.setText("");
        txharga.setText("");
        txdesk.setText("");
    }

    
    private void loadKategori() throws SQLException {
        Connection cnn = koneksi();
        PreparedStatement PS = cnn.prepareStatement("SELECT nama_kategori FROM category");
        ResultSet RS = PS.executeQuery();

        cbkategori.removeAllItems(); 
        while (RS.next()) {
            String kategori = RS.getString("nama_kategori");
            cbkategori.addItem(kategori); 
        }
    }
    
    private int getKategoriId(String namaKategori) throws SQLException {
        Connection cnn = koneksi();
        PreparedStatement PS = cnn.prepareStatement("SELECT id FROM category WHERE nama_kategori=?");
        PS.setString(1, namaKategori);
        ResultSet RS = PS.executeQuery();

        if (RS.next()) {
            return RS.getInt("id");
        } else {
            throw new SQLException("Kategori tidak ditemukan");
        }
    }

    private void dtProdukList() throws SQLException {
        Connection cnn = koneksi();
        PreparedStatement PS = cnn.prepareStatement(
            "SELECT i.id, i.kode, i.nama, i.deskripsi, i.harga, i.stok, c.nama_kategori " +
            "FROM item i LEFT JOIN category c ON i.id_kategori = c.id"
        );
        ResultSet RS = PS.executeQuery();

        TM.getDataVector().removeAllElements();
        TM.fireTableDataChanged();

        while (RS.next()) {
            Object[] dta = new Object[7];
            dta[0] = RS.getInt("id");
            dta[1] = RS.getString("kode");
            dta[2] = RS.getString("nama");
            dta[3] = RS.getString("nama_kategori");
            dta[4] = RS.getString("deskripsi");
            dta[5] = RS.getDouble("harga");
            dta[6] = RS.getInt("stok");
            
            TM.addRow(dta);
        }
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        DTM = new javax.swing.JTable();
        txnama = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txkode = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txstok = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txharga = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txdesk = new javax.swing.JTextArea();
        btnTambah = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        btnUpdate = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnTutup = new javax.swing.JButton();
        cbkategori = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        DTM.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        DTM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DTMMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(DTM);

        txnama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txnamaActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Form Input");

        jLabel2.setText("Nama Barang");

        jLabel3.setText("Kode Barang");

        jLabel4.setText("Kategori");

        txkode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txkodeActionPerformed(evt);
            }
        });

        jLabel5.setText("Stok");

        txstok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txstokActionPerformed(evt);
            }
        });

        jLabel6.setText("Harga");

        txharga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txhargaActionPerformed(evt);
            }
        });

        jLabel7.setText("Deskripsi");

        txdesk.setColumns(20);
        txdesk.setRows(5);
        jScrollPane2.setViewportView(txdesk);

        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel8.setText("Data Inventory");

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnTutup.setText("Tutup");
        btnTutup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTutupActionPerformed(evt);
            }
        });

        cbkategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 709, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txstok)
                            .addComponent(txnama)
                            .addComponent(txkode)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(cbkategori, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txharga)
                            .addComponent(jScrollPane2)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(173, 173, 173)
                        .addComponent(btnTambah)
                        .addGap(35, 35, 35)
                        .addComponent(btnUpdate)
                        .addGap(38, 38, 38)
                        .addComponent(btnHapus)
                        .addGap(30, 30, 30)
                        .addComponent(btnTutup))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel8)))
                .addGap(18, 18, 18))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(49, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txnama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txkode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbkategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txstok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txharga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambah)
                    .addComponent(btnUpdate)
                    .addComponent(btnHapus)
                    .addComponent(btnTutup))
                .addGap(27, 27, 27))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txnamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txnamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txnamaActionPerformed

    private void txkodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txkodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txkodeActionPerformed

    private void txstokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txstokActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txstokActionPerformed

    private void txhargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txhargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txhargaActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        if(btnTambah.getText().equals("Tambah")){
            aturMode("tambah");
        }else{
            try {
                tambahData();
                dtProdukList();
                JOptionPane.showMessageDialog(this, "Produk berhasil ditambahkan!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan produk: " + e.getMessage());
            }
            aturMode("awal");
        }
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        if(idProdukTerpilih == -1){
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit terlebih dahulu.");
            return;
        }
        
        if(btnUpdate.getText().equals("Edit")){
            aturMode("edit");
        }else{
            try {
                updateData();
                dtProdukList();
                JOptionPane.showMessageDialog(this, "Data berhasil diupdate!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal mengupdate data: " + e.getMessage());
            }
            aturMode("awal");
        }
        
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        if (idProdukTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus.");
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(this,
            "Yakin ingin menghapus produk ini?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION
        );
        
        if (konfirmasi == JOptionPane.YES_OPTION) {
            try {
                destroyData();
                dtProdukList();
                JOptionPane.showMessageDialog(this, "Produk berhasil dihapus!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus produk: " + e.getMessage());
            }

            aturMode("awal");
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnTutupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTutupActionPerformed
        if (btnTutup.getText().equals("Tutup")) {
            int pilihan = JOptionPane.showConfirmDialog(this, "Tutup aplikasi?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (pilihan == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        } else {
            aturMode("awal");
            
        }
    }//GEN-LAST:event_btnTutupActionPerformed

    private void DTMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DTMMouseClicked
        int row = DTM.getSelectedRow();
        if(row >=0 ){
            idProdukTerpilih = Integer.parseInt(DTM.getValueAt(row, 0).toString()); // Ambil ID dari kolom pertama
            txkode.setText(DTM.getValueAt(row, 1).toString());
            txnama.setText(DTM.getValueAt(row, 2).toString());
            cbkategori.setSelectedItem(DTM.getValueAt(row, 3).toString());
            txdesk.setText(DTM.getValueAt(row, 4).toString());
            txharga.setText(DTM.getValueAt(row, 5).toString());
            txstok.setText(DTM.getValueAt(row, 6).toString());            
            aturMode("pilih");
        }
    }//GEN-LAST:event_DTMMouseClicked

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
            java.util.logging.Logger.getLogger(formInventory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(formInventory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(formInventory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(formInventory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new formInventory().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(formInventory.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable DTM;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnTutup;
    private javax.swing.JButton btnUpdate;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbkategori;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea txdesk;
    private javax.swing.JTextField txharga;
    private javax.swing.JTextField txkode;
    private javax.swing.JTextField txnama;
    private javax.swing.JTextField txstok;
    // End of variables declaration//GEN-END:variables
}
