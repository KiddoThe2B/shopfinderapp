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
import kiddo.android.ProductAdapter;
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
    ImageView imageView;
    private Button btn1;
    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.product);
       Log.d("ShopFinder","Product.onCreate() called!");
       Intent intent = getIntent();
       Product product = (Product) intent.getSerializableExtra("product");
       name = (TextView)findViewById(R.id.product_name);
       name.setText(product.getName());
       desc = (TextView)findViewById(R.id.product_desc);
       desc.setText(product.getDescription());
       imageView = (ImageView)findViewById(R.id.product_image);
       if(product.getImagebtm()!=null){
        Log.d("ShopFinder","Done");
        Log.d("ShopFinder","HEIGHT:"+product.getImagebtm().getHeight());
       }
       imageView.setImageBitmap(product.getImagebtm());  
       imageView.setVisibility(View.VISIBLE);
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
   
    public void viewCart(View v){
        
    }

   
}
