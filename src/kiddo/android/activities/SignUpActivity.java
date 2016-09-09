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
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import kiddo.android.R;
import kiddo.android.ShopFinderApplication;

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
                    new SignUpActivity.RESTClient().execute(((ShopFinderApplication) this.getApplication()).getIP());
                    sleep(2000);
                    Log.d("ShopFinder", "Data: "+data);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
        data = downloadUrl(urls[0]+"/shopfinder/signup");
        Log.d(("ShopFinder"), urls[0]+"/shopfinder/signup");
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
            conn.setRequestMethod("POST");
            Map<String,Object> params = new LinkedHashMap();
            params.put("email", email.getText().toString());
            params.put("password", password.getText().toString());
            params.put("fullname", fullname.getText().toString());
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            Log.d("ShopFinder","email: "+email.getText().toString()+", password: "+password.getText().toString());
            conn.setRequestProperty("email", email.getText().toString());
            conn.setRequestProperty("password", password.getText().toString());
            conn.setRequestProperty("fullname", fullname.getText().toString());
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
