/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kiddo.android.models;

import org.json.JSONObject;


/**
 *
 * @author kiddo
 */
public class Item {
    
    private User user;
    private Product product;
    
    public Item() {
    }

    Item(JSONObject obj) {
        
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    


}
