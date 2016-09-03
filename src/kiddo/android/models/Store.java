/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kiddo.android.models;

import java.io.Serializable;
import java.util.List;


/**
 *
 * @author Francisco Javier munoz
 */

public class Store implements Serializable{
    
    int id;
    String name;
    String address;
    String image;
    List<Store_Product> store_products;
    

    public int getStoreId(){
        return id;
    }
    
    public void setStoreId(int id){
        this.id = id;
    }

    public Store() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
    public List<Store_Product> getStore_Products() {
        return store_products;
    }

    public void setStore_Products(List<Store_Product> store_products) {
        this.store_products = store_products;
    }
}

