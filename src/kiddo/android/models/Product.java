/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kiddo.android.models;

import android.graphics.Bitmap;
import java.io.Serializable;
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


    public Product(JSONObject obj) {
        try {
            id = obj.getInt("id");
            name = obj.getString("name");
            description = obj.getString("description");
            image = obj.getString("image");

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
    
    @Override
    public String toString() {
        return this.id + ". " + this.name;
    }
}
