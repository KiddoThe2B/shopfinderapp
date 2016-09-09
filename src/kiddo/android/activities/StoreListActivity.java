package kiddo.android.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import kiddo.android.ProductArrayAdapter;
import kiddo.android.R;
import kiddo.android.ShopFinderApplication;
import kiddo.android.StoreArrayAdapter;
import static kiddo.android.activities.CatalogActivity.products;
import kiddo.android.models.Item;
import kiddo.android.models.Product;
import kiddo.android.models.Store;
import kiddo.android.models.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static java.lang.Thread.sleep;

public class StoreListActivity extends ListActivity
{
    String data;
    String response;
    static List<Store> stores;
    Product product;
    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       Log.d("ShopFinder","StoreList.onCreate() called!");
       Intent intent = getIntent();
       product = (Product) intent.getSerializableExtra("product");
       Log.d("ShopFinder","1 product_id:"+product.getProductId());
        try {
            this.findShops();
        } catch (InterruptedException ex) {
            stores = new ArrayList<Store>();
        }
        Log.d("ShopFinder","2");
        setListAdapter(new StoreArrayAdapter(this, stores,1));
        getListView().setBackgroundColor(0xFFFFFF);
        getListView().invalidate();
        Log.d("ShopFinder", "Ready data:"+stores.size());
        if(stores.isEmpty())
            Toast.makeText(this, "The list is empty", Toast.LENGTH_SHORT).show();
    }
    
    public void findShops()throws InterruptedException{
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new StoreListActivity.RESTShops().execute(((ShopFinderApplication) this.getApplication()).getIP());
            sleep(2000);
            if (!response.isEmpty()){
                    stores = new ArrayList();
                        try {
                            Log.d("ShopFinder","1.1");
                            JSONArray array = new JSONArray(response);
                            for(int i=0; i<array.length();i++){
                                JSONObject obj = array.getJSONObject(i);
                                JSONObject store = obj.getJSONObject("pk").getJSONObject("store");
                                double price = obj.getDouble("price");
                                Log.d("ShopFinder","1.2");
                                stores.add(new Store(store));
                                Log.d("ShopFinder","1.3");
                                stores.get(stores.size()-1).setPrice(price);
                            }
                            Log.d("ShopFinder","1.4");
                        } catch (JSONException ex) {

                        }

            }
            else{
                Toast.makeText(this, "Unable to add to cart", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(this, "No network connection available.", Toast.LENGTH_LONG).show();
        }
    }
    
    public void buyItem(View v){
       // SAVE AND GO TO REVIEW
//       Toast.makeText(this, "Can't afford it!", Toast.LENGTH_LONG).show();
        Log.d("ShopFinder","StoreListActivity.buyItem() called");
        int index = (Integer) v.getTag();
        Log.d("ShopFinder","store_id: "+index);
        Intent intent = new Intent(getApplicationContext(), ReviewsActivity.class);
        intent.putExtra("store", (Serializable) stores.get(index));
        intent.putExtra("product", (Serializable) product);
        Log.d("ShopFinder", "Preparing new intent");
        startActivity(intent);
    }
    
    public void callMaps(View v)throws InterruptedException{
        // SHOW MAP
        Log.d("ShopFinder","StoreListActivity.callMaps() called");
        int index = (Integer) v.getTag();
        Log.d("ShopFinder","store_id: "+index);
        String uri = String.format(Locale.ENGLISH, "geo:0,0?q=%s", stores.get(index).getAddress().replace(" ", "+"));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
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
    
    class RESTShops extends AsyncTask<String, Void, String> {

    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... urls) {
        response = downloadUrl(urls[0]+"/shopfinder/product/"+product.getProductId()+"/storeproducts");
        return response;
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
    
}
