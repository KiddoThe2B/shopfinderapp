/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kiddo.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import kiddo.android.R;
import kiddo.android.models.Product;
import kiddo.android.models.Store;

/**
 *
 * @author Francisco Javier
 */
public class ReviewsActivity extends Activity {
    
  private Product product;
  private Store store;
  private TextView text;
  private Spinner spinner;
  private EditText review;
  private Button btnSubmit;
    
  @Override
  public void onCreate(Bundle savedInstanceState) {
        Log.d("ShopFinder","Reviews.onCreate() called!");
	super.onCreate(savedInstanceState);
        setContentView(R.layout.reviews);
        Intent intent = getIntent();
        product = (Product) intent.getSerializableExtra("product");
        store = (Store) intent.getSerializableExtra("store");
        text = (TextView)findViewById(R.id.review_product);       
        text.setText(product.getName());
        text = (TextView)findViewById(R.id.review_store);       
        text.setText(store.getName());
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayList<Integer> numbers = new ArrayList();
        for (int i = 1; i <= 5; i++) numbers.add(i);
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this,
        android.R.layout.simple_spinner_item, numbers);
	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spinner.setAdapter(dataAdapter);
        review =(EditText)findViewById(R.id.review_input);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
    
  }
  
     public void submit(View view) {
       
     Log.d("ShopFinder","Reviews.onSubmit() called!");
     String rating = spinner.getSelectedItem().toString();
     String review_str = null;
     if(this.review.getText().toString().length()!=0) review_str = this.review.getText().toString();
     
     Log.d("ShopFinder","Reviews.onSubmit() called! "+ rating +" "+ review_str);
     
     }
}