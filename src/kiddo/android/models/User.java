/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kiddo.android.models;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author kiddo
 */

public class User {

    int user_id = 0;
    String fullname = "";
    String email = "";
    String password = "";
    List<Item> cart =  new ArrayList();

    public User(String data) {
        try {
            JSONObject reader = new JSONObject(data);
            user_id = reader.getInt("user_id");
            email = reader.getString("email");
            password = reader.getString("password");
            fullname = reader.getString("fullname");
            JSONArray array = reader.getJSONArray("cart");
            for(int i=0; i<array.length();i++){
                JSONObject obj = array.getJSONObject(i);
                cart.add(new Item(obj));
            }
        } catch (JSONException ex) {
            
        }
    }

    
    
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public List<Item> getCart() {
        return cart;
    }

    public void setCart(List<Item> cart) {
        this.cart = cart;
    }
    
}
