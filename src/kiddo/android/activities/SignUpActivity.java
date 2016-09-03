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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import static java.lang.Thread.sleep;
import java.net.HttpURLConnection;
import java.net.URL;
import kiddo.android.R;

public class SignUpActivity extends Activity
{
    private EditText email;
    private EditText password;
    private EditText fullname;
    private Button btn;
    private String data;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        email=(EditText)findViewById(R.id.email);
        password =(EditText)findViewById(R.id.password);
        fullname =(EditText)findViewById(R.id.fullname);
        btn=(Button)findViewById(R.id.signup_btn);
        Log.d("ShopFinder","onCreate() called!");
    }
    
    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d("ShopFinder","onPause() called!");
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d("ShopFinder","onDestroy() called!");
    }
    
    public void signUp(View v) throws InterruptedException {
        Log.d("ShopFinder","signIn() called!");
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if(email.getText().toString().length()==0 || password.getText().toString().length()==0){
                    Toast.makeText(this, "Please fill information", Toast.LENGTH_LONG).show();
            }else{
                    new RESTClient().execute(email.getText().toString());
                    sleep(2000);
                    Log.d("ShopFinder", "Data: "+data);
                    Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(intent);
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
        try {
            data = downloadUrl("http://192.168.1.3:8084/shopfinder/product/1");
            return data;
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        data = result;
    }
    
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;
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
            Log.d("ShopFinder", "The response is: " + contentAsString);
            return contentAsString;

        // Makes sure that the InputStream is closed after the app is
        // finished using it.
        } finally {
            if (is != null) {
                is.close();
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
