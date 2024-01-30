
package com.groupc.saleberry.Model;

public class Inventory {
    private int category_id;
    private String category_name;
    private String product_name;
    private int product_id;
    private int price;
    public Inventory(int category_id, String category_name,int product_id, String product_name, int price){
        this.category_id = category_id;
        this.category_name = category_name;
        this.product_name = product_name;
        this.product_id = product_id;
        this.price = price;
    }
    public int getCategory_id(){
        return this.category_id;
    }
    public String getCategory_name(){
        return this.category_name;
    } 
    
    public String getProduct_name(){
        return this.product_name;
    }
    
    public int getProduct_id(){
        return this.product_id;
    }
    
    public int getPrice(){
        return this.price;
    }    
}
