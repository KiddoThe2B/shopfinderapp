/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kiddo.android.activities;

/**
 *
 * @author Francisco Javier
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import kiddo.android.R;

public class SettingsActivity extends Activity {
    
  private RadioGroup radioSearchGroup;
  private RadioButton radioSearchButton;
  private Button btnSearch;
  private Spinner spinner;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
        Log.d("ShopFinder","Settings.onCreate() called!");
	super.onCreate(savedInstanceState);
	setContentView(R.layout.settings);
        spinner = (Spinner) findViewById(R.id.spinner);
        radioSearchGroup = (RadioGroup) findViewById(R.id.radioSearch);
	btnSearch = (Button) findViewById(R.id.btnSearch);
        ArrayList<Integer> numbers = new ArrayList();
        for (int i = 0; i <= 100; i++){
           numbers.add(i);
        }
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this,
		android.R.layout.simple_spinner_item, numbers);
	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spinner.setAdapter(dataAdapter);
  }
  
   public void apply(View view) {
       
     Log.d("ShopFinder","Settings.onApply() called!");
     
     radioSearchGroup = (RadioGroup) findViewById(R.id.radioSearch);
     int modeId = radioSearchGroup.getCheckedRadioButtonId();
     String dist = spinner.getSelectedItem().toString();
     
     SharedPreferences sharedPref = SettingsActivity.this.getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
     SharedPreferences.Editor editor = sharedPref.edit();
     editor.putInt(getString(R.string.mode), modeId);
     editor.putString(getString(R.string.distance), dist);
     editor.commit();
     
     int mode = sharedPref.getInt(getString(R.string.mode), 0);
     dist = sharedPref.getString(getString(R.string.distance), null);
     Log.d("ShopFinder","Settings.onApply() called! "+ mode +" "+ dist);
 }


}
