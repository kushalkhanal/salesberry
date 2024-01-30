/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.groupc.saleberry.Model;


public class IconImage {

    public IconImage(int s_num,byte[] picture){
        this.s_num = s_num;
        this.picture = picture;
    }
    public int getS_num() {
        return s_num;
    }

    /**
     * @param s_num the s_num to set
     */
    public void setS_num(int s_num) {
        this.s_num = s_num;
    }

    /**
     * @return the picture
     */
    public byte[] getPicture() {
        return picture;
    }

    /**
     * @param picture the picture to set
     */
    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
  private int s_num; 
  private byte[] picture;
    public IconImage(byte[] picture){
        this.picture = picture;
    }
}



