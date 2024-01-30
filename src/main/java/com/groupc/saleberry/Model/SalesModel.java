
package com.groupc.saleberry.Model;

public class SalesModel {

    public SalesModel(int category_id, String category_name,int product_id, String product_name, int price,int quantity,int totalPrice){
        this.category_id = category_id;
        this.category_name = category_name;
        this.product_name = product_name;
        this.product_id = product_id;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
    public int getCategory_id() {
        return category_id;
    }

    /**
     * @return the category_name
     */
    public String getCategory_name() {
        return category_name;
    }

    /**
     * @return the product_name
     */
    public String getProduct_name() {
        return product_name;
    }

    /**
     * @return the product_id
     */
    public int getProduct_id() {
        return product_id;
    }

    /**
     * @return the price
     */
    public int getPrice() {
        return price;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @return the totalPrice
     */
    public int getTotalPrice() {
        return totalPrice;
    }
    private int category_id;
    private String category_name;
    private String product_name;
    private int product_id;
    private int price;
    private int quantity;
    private int totalPrice;
}
