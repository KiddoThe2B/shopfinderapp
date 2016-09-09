/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kiddo.android.models;

import android.graphics.Bitmap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




/**
 *
 * @author kiddo
 */

public class Product implements Serializable{
    
    int id;
    String name;
    String description;
    String image;
    Bitmap imagebtm;
    List<Store> stores;
    double price;

    public Product(JSONObject obj) {
        try {
            id = obj.getInt("id");
            name = obj.getString("name");
            description = obj.getString("description");
            image = obj.getString("image");
            JSONArray store_products = obj.getJSONArray("store_products");
            price = Double.MAX_VALUE; 
            stores = new ArrayList();
            for(int i=0; i<store_products.length();i++){
                Store store = new Store(store_products.getJSONObject(i).getJSONObject("pk").getJSONObject("store"));
                store.setPrice(store_products.getJSONObject(i).getDouble("price"));
                stores.add(store);
                double temp =store_products.getJSONObject(i).getDouble("price");
                if(temp < price){
                    price = temp;
                }
            }    
        } catch (JSONException ex) {
            
        }
    }
    
    public int getProductId(){
        return id;
    }
    
    public void setProductId(int id){
        this.id = id;
    }

    public Product() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }  

    public Bitmap getImagebtm() {
        return imagebtm;
    }

    public void setImagebtm(Bitmap imagebtm) {
        this.imagebtm = imagebtm;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    @Override
    public String toString() {
        return this.id + ". " + this.name;
    }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }
    
    
}
