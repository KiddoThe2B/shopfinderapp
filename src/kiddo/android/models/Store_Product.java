/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kiddo.android.models;

import java.util.List;


public class Store_Product {
    

    private int storeproduct_id;
    private double price;
    private Store store;
    private Product product;
    
    public Store_Product(){
    }
    

    public int getStoreproduct_id() {
        return storeproduct_id;
    }

    public void setStoreproduct_id(int storeproduct_id) {
        this.storeproduct_id = storeproduct_id;
    }
    
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    
    

}
