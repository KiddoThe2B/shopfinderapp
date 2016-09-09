package kiddo.android.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.Comparator;
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
import kiddo.android.services.GPSTracker;
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
       SharedPreferences sharedPref = StoreListActivity.this.getSharedPreferences(getString(R.string.distance),Context.MODE_PRIVATE);
       String distance = sharedPref.getString(getString(R.string.distance), "0");
       String coords="0";
        try {
            coords = this.getCoords();
        } catch (InterruptedException ex) {
            Logger.getLogger(StoreListActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
//        stores = product.getStores();
        try {
            this.filterShops(distance,coords);
        } catch (InterruptedException ex) {
            stores = new ArrayList<Store>();
        }
        Log.d("ShopFinder","2");
        setListAdapter(new StoreArrayAdapter(this, stores,1));
        StoreArrayAdapter sad = (StoreArrayAdapter) getListAdapter();
//        sad.sortByPrice();
        getListView().setBackgroundColor(0xFFFFFF);
        getListView().invalidate();
        Log.d("ShopFinder", "Ready data:"+stores.size());
        if(stores.isEmpty())
            Toast.makeText(this, "The list is empty", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //RelativeLayout layout = (RelativeLayout) findViewById(R.layout.activity_home);

        getMenuInflater().inflate(R.menu.storelist_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        StoreArrayAdapter sad = (StoreArrayAdapter) getListAdapter();
        switch (item.getItemId()) {
            case R.id.sortprice:
                sad.sortByPrice();
                return true;
            case R.id.sortdistance:
                sad.sortByPrice();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
    
    public void filterShops(String distance, String coords)throws InterruptedException{
        for(Store store: stores){
//            new StoreListActivity.GoogleDirections().execute(coords,store.getAddress());
//            sleep(2000);
//            JSONObject obj = new JSONObject(data);
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
        // check if GPS enabled
        GPSTracker gpsTracker = new GPSTracker(this);
        sleep(2000);
        if (gpsTracker.getIsGPSTrackingEnabled())
        {
            Log.d("ShopFinder","location: ("+gpsTracker.getLatitude()+","+gpsTracker.getLongitude()+")");
            String coords = gpsTracker.getLatitude()+","+gpsTracker.getLongitude();
            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%s&daddr=%s", coords, stores.get(index).getAddress().replace(" ", "+"));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "GPS not enabled", Toast.LENGTH_LONG).show();

        }

    }
    
    public String getCoords() throws InterruptedException{
        GPSTracker gpsTracker = new GPSTracker(this);
        sleep(2000);
        if (gpsTracker.getIsGPSTrackingEnabled())
        {
            return gpsTracker.getLatitude()+","+gpsTracker.getLongitude();
        }
        else
        {
            return "0";

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
    
         class GoogleDirections extends AsyncTask<String, Void, String> {

    
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... urls) {
            data = downloadUrl("https://maps.googleapis.com/maps/api/directions/json?origin="+urls[0]+"&destination="+urls[1].replace(" ", "+")+"&key=AIzaSyAAeu0lGsiJN566w3899Cxl7K2NEzurFYU");
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
                contentAsString =contentAsString.substring(0, contentAsString.lastIndexOf("}")+1);
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
