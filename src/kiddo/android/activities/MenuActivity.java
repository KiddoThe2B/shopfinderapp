package kiddo.android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import static java.lang.Thread.sleep;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import kiddo.android.R;
import kiddo.android.ShopFinderApplication;
import kiddo.android.models.User;

public class MenuActivity extends Activity
{
    String data;
    static String catalog;
    private TextView greetings;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.menu);
       Log.d("ShopFinder","Menu.onCreate() called!");
       Intent intent = getIntent();
       String message = "Hi "+ ((ShopFinderApplication) this.getApplication()).getUser().getFullname() + "!";
       greetings = (TextView)findViewById(R.id.greetings);
       greetings.setText(message);
       btn1=(Button)findViewById(R.id.viewcart_btn);
       btn2=(Button)findViewById(R.id.searchproducts_btn);
       btn3=(Button)findViewById(R.id.searchstores_btn);

       Log.d("ShopFinder","Menu.onCreate() called!");
    }
    
    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d("ShopFinder","Menu.onPause() catalog called!");
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d("ShopFinder","Menu.onDestroy() catalog called!");
    }
   
    public void viewCart(View v){
        Intent intent = new Intent(getApplicationContext(), CartActivity.class);
        Log.d("ShopFinder", "Preparing new intent");
        startActivity(intent);
    }
    
    public void searchStores(View v){
        Intent intent = new Intent(getApplicationContext(), StoreSearchActivity.class);
        Log.d("ShopFinder", "Preparing new intent");
        startActivity(intent);
    }
    
    public void searchProducts(View v) throws InterruptedException{
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
             new MenuActivity.RESTClient().execute(((ShopFinderApplication) this.getApplication()).getIP());
             sleep(2000);
             Log.d("ShopFinder", "Data: "+data);

            if (((ShopFinderApplication) this.getApplication()).getUser().getUser_id()!=0){
                Intent intent = new Intent(getApplicationContext(), CatalogActivity.class);
                intent.putExtra(catalog, data);
                Log.d("ShopFinder", "Preparing new intent");
                startActivity(intent);
            }
            else{
                Toast.makeText(this, "Unable to serve catalog. Server not responding!", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(this, "No network connection available.", Toast.LENGTH_LONG).show();
        }
    }

    class RESTClient extends AsyncTask<String, Void, String> {

    
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... urls) {
            data = downloadUrl(urls[0]+"/shopfinder/catalog");
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        private String downloadUrl(String myurl){
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
            int len = 5000;
            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(20000 /* milliseconds */);
                conn.setConnectTimeout(25000 /* milliseconds */);
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
