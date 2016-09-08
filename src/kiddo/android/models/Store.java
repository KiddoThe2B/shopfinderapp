/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kiddo.android.models;

import java.io.Serializable;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author Francisco Javier munoz
 */

public class Store implements Serializable{
    
    int id;
    String name;
    String address;
    String image;
    double price;

    public int getStoreId(){
        return id;
    }
    
    public void setStoreId(int id){
        this.id = id;
    }

    public Store(JSONObject obj) {
        try {
            id = obj.getInt("id");
            name = obj.getString("name");
            address = obj.getString("address");
            image = obj.getString("image");
        } catch (JSONException ex) {
            
        }
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

