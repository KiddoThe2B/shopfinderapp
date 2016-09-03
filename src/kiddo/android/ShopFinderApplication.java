/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kiddo.android;

import android.app.Application;
import kiddo.android.models.User;

/**
 *
 * @author kiddo
 */
public class ShopFinderApplication extends Application {
    User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
