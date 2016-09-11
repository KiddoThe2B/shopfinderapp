package kiddo.android.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import static java.lang.Thread.sleep;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import kiddo.android.ProductArrayAdapter;
import kiddo.android.R;
import kiddo.android.ShopFinderApplication;
import static kiddo.android.activities.CatalogActivity.products;
import kiddo.android.models.Item;
import kiddo.android.models.Product;
import kiddo.android.models.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CartActivity extends ListActivity
{
    String data;
    int user_id;
    int product_id;
    String response;
    static List<Product> products;
    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       Log.d("ShopFinder","Cart.onCreate() called!");
        try {
            products = this.getCart(((ShopFinderApplication) this.getApplication()).getUser().getUser_id());
        } catch (InterruptedException ex) {
            Logger.getLogger(CartActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
        setListAdapter(new ProductArrayAdapter(this, products,1));
        getListView().setBackgroundColor(0xFFFFFF);
        getListView().invalidate();
        Log.d("ShopFinder", "Ready data:"+products.size());
        if(products.isEmpty())
            Toast.makeText(this, "The list is empty", Toast.LENGTH_SHORT).show();
    }
    
    public void selectItem(View v){
        TextView tv = (TextView)v;
        String text = tv.getText().toString();
        product_id = (Integer) tv.getTag();
        Log.d("ShopFinder","Cart.selectItem() catalog called! ");
        Product product =null;
        for(Product prod :products){
            if(prod.getProductId()==product_id){
                product = prod;
                break;
            }
        }
        Intent intent = new Intent(getApplicationContext(), StoreListActivity.class);
        intent.putExtra("product", (Serializable) product);
        startActivity(intent);

    }
    
    public void handleItem(View v)throws InterruptedException{
        Log.d("ShopFinder","Catalog.selectItem() catalog called!");
        Button b = (Button)v;
        product_id = (Integer) b.getTag();
        for(Product prod: products){
            if(prod.getProductId()==product_id){
                products.remove(prod);
            }
        }
        ListAdapter adap = getListAdapter();
        Log.d("ShopFinder","ProductActivity.removeFromCart() Product id:"+product_id);
        user_id = ((ShopFinderApplication) this.getApplication()).getUser().getUser_id();
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new CartActivity.RESTClient2().execute(((ShopFinderApplication) this.getApplication()).getIP());
            sleep(2000);
            if (!response.isEmpty()){
                setListAdapter(new ProductArrayAdapter(this, products,1));
                Toast.makeText(this, "Product removed from cart successfully", Toast.LENGTH_LONG).show();
                if(products.isEmpty())
                    Toast.makeText(this, "The list is empty", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Unable to remove from cart", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(this, "No network connection available.", Toast.LENGTH_LONG).show();
        }
    }
    
    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d("ShopFinder","Catalog.onPause() catalog called!");
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d("ShopFinder","Catalog.onDestroy() catalog called!");
    }
    
    public List<Product> getCart(int user_id) throws InterruptedException{
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        this.user_id = user_id;
        if (networkInfo != null && networkInfo.isConnected()) {
            new CartActivity.RESTClient().execute(((ShopFinderApplication) this.getApplication()).getIP());
            sleep(2000);
            Log.d("ShopFinder", "Data: "+data);
            List<Product> list = new ArrayList();
            try {
                JSONArray array = new JSONArray(data);
                for(int i=0; i<array.length();i++){
                    JSONObject obj = array.getJSONObject(i);
                    list.add(new Product(obj));
                }
            } catch (JSONException ex) {

            }

            return list;
            
        }
        else {
            Toast.makeText(this, "No network connection available.", Toast.LENGTH_LONG).show();
            return null;
        }
        
    }
    

    class RESTClient extends AsyncTask<String, Void, String> {

    
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... urls) {
            data = downloadUrl(urls[0]+"/shopfinder/user/"+user_id+"/cart");
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        private String downloadUrl(String myurl){
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
            int len = 10000;
            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                is = conn.getInputStream();

                // Convert the InputStream into a string
                String contentAsString = readIt(is, len);
                contentAsString =contentAsString.substring(0, contentAsString.lastIndexOf("]")+1);
                Log.d("ShopFinder", "The response is: " + contentAsString);
                return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
            } 
            catch(Exception ex){
                Log.d("ShopFinder", "GET failed!");
                return "";
            }
            finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException ex) {
                    }
                }
            }
        }

        // Reads an InputStream and converts it to a String.
        public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }
    }
    class RESTClient2 extends AsyncTask<String, Void, String> {

    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... urls) {
        response = downloadUrl(urls[0]+"/shopfinder/removefromcart");
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
    }
    
    private String downloadUrl(String myurl){
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            Map<String,Object> params = new LinkedHashMap();
            params.put("user_id",user_id);
            params.put("product_id", product_id);

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");;
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            Log.d("ShopFinder", "The response is: " + contentAsString);
            return contentAsString;

        // Makes sure that the InputStream is closed after the app is
        // finished using it.
        } 
        catch(Exception ex){
            Log.d("ShopFinder", "POST failed!");
            return "";
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                }
            }
        }
    }
    
    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
    }
    
}
