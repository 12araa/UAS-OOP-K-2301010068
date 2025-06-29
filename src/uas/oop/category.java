/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uas.oop;

/**
 *
 * @author R
 */
public class category {
    private int id;
    private String nama;

    public category(int id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    // Getter & Setter
    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }
    
     public void setId(int id) {
        this.id = id;
    }

    public void setName(String nama) {
        this.nama = nama;
    }

    // Untuk menampilkan di ComboBox
    @Override
    public String toString() {
        return nama;
    }
}
