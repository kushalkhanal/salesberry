/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.groupc.saleberry.Model;

public class Sell {

    public Sell( ){
//        this.category_id = category_id;
//        this.category_name = category_name;
//        this.product_name = product_name;
//        this.product_id = product_id;
//        this.price = price;
//        this.quantity = quantity;
//        this.total_price = total_price;
    }
    public int getCategory_id() {
        return category_id;
    }

    /**
     * @param category_id the category_id to set
     */
    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    /**
     * @return the category_name
     */
    public String getCategory_name() {
        return category_name;
    }

    /**
     * @param category_name the category_name to set
     */
    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    /**
     * @return the product_id
     */
    public int getProduct_id() {
        return product_id;
    }

    /**
     * @param product_id the product_id to set
     */
    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    /**
     * @return the product_name
     */
    public String getProduct_name() {
        return product_name;
    }

    /**
     * @param product_name the product_name to set
     */
    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    /**
     * @return the price
     */
    public int getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the total_price
     */
    public int getTotal_price() {
        return total_price;
    }

    /**
     * @param total_price the total_price to set
     */
    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }
    private int category_id;
    private String category_name;
    private int product_id;
    private String product_name;
    private int price;
    private int quantity;
    private int total_price;
    
}
