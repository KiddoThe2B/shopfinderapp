package kiddo.android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import kiddo.android.R;
import kiddo.android.ShopFinderApplication;
import kiddo.android.models.Item;
import kiddo.android.models.Product;
import kiddo.android.models.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductActivity extends Activity
{
    private TextView name;
    private TextView desc;
    private TextView id;
    private String response;
    private int user_id;
    private int product_id;
    ImageView imageView;
    private Button btn1;
    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.product);
       Log.d("ShopFinder","Product.onCreate() called!");
       Intent intent = getIntent();
       Log.d("ShopFinder","1");
       Product product = (Product) intent.getSerializableExtra("product");
       Log.d("ShopFinder","2");
       id = (TextView)findViewById(R.id.product_id);       
       id.setText(Integer.toString(product.getProductId()));
       Log.d("ShopFinder","3");
       name = (TextView)findViewById(R.id.product_name);
       name.setText(product.getName());
       desc = (TextView)findViewById(R.id.product_desc);
       desc.setText(product.getDescription());
       Log.d("ShopFinder","4");
//       imageView = (ImageView)findViewById(R.id.product_image);
//       if(product.getImagebtm()!=null){
//        Log.d("ShopFinder","Done");
//        Log.d("ShopFinder","HEIGHT:"+product.getImagebtm().getHeight());
//       }
//       imageView.setImageBitmap(product.getImagebtm());  
//       imageView.setVisibility(View.VISIBLE);
    }
    
    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d("ShopFinder","Product.onPause() catalog called!");
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d("ShopFinder","Product.onDestroy() catalog called!");
    }
   
    public void addToCart(View v) throws InterruptedException{
        Log.d("ShopFinder","ProductActivity.addToCart() Product id:"+id.getText().toString());
        product_id = Integer.parseInt(id.getText().toString());
        user_id = ((ShopFinderApplication) this.getApplication()).getUser().getUser_id();
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new ProductActivity.RESTClient().execute(((ShopFinderApplication) this.getApplication()).getIP());
            sleep(2000);
            if (!response.isEmpty()){
                Toast.makeText(this, "Product added to cart successfully", Toast.LENGTH_LONG).show();

            }
            else{
                Toast.makeText(this, "Unable to add to cart", Toast.LENGTH_LONG).show();
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
        response = downloadUrl(urls[0]+"/shopfinder/addtocart");
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
