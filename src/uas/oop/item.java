/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uas.oop;

/**
 *
 * @author R
 */
public class item {
    private String id;
    private String kode;
    private String nama;
    private String deskripsi;
    private double harga;
    private int stok;
    
    public item (String id, String kode, String nama, String deskripsi, double harga, int stok){
        this.id = id;
        this.kode = kode;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.stok = stok;
    }
    
    public String getId(){
        return id;
    }
    
    public String getKode(){
        return kode;
    }
    
    public String getNama(){
        return nama;
    }
    
    public String getDesc(){
        return deskripsi;
    }
    
    public double getHarga(){
        return harga;
    }
    
    public int getStok(){
        return stok;
    }
    
    public void setKode(String kode){
        this.kode = kode;
    }
    
    public void setNama(String nama){
        this.nama = nama;
    }
    
    public void setDesc(String deskripsi){
        this.deskripsi = deskripsi;
    }
    
    public void setHarga(double harga){
        this.harga = harga;
    }
    
    public void setStok(int stok){
        this.stok = stok;
    }
    
    @Override
    public String toString(){
        return "ID: "+id+"  Nama: "+nama+"  Harga: Rp "+String.format("%.2f",harga)+"  Stok: "+stok;
    }
}
