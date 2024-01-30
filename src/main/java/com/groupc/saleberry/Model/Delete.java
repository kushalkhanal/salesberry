/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.groupc.saleberry.Model;


public class Delete {
    private int category_id;
    private int product_id;
    private String category_name;
    private String product_name;
    private int price;
    public Delete(int category_id, int product_id){
        this.category_id = category_id;
        this.product_id = product_id;

    }
    public int getCategory_id(){
        return this.category_id;
    }
    
    public int getProduct_id(){
        return this.product_id;
    }
    
    public void setCategory_id(int category_id){
        this.category_id= category_id;
    }
    
    public void setProduct_id(int product_id){
        this.product_id= product_id;
    }
    public void setCategoryName(String category_name){
        this.category_name= category_name;
    }
    public void setProductName(String product_name){
        this.product_name= product_name;
    }
    public void setPrice(int price){
        this.price= price;
    }
        
}


