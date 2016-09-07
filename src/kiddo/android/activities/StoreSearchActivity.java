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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import kiddo.android.R;

public class StoreSearchActivity extends Activity {
    
  private RadioGroup radioSearchGroup;
  private RadioButton radioSearchButton;
  private Button btnSearch;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
        Log.d("ShopFinder","StoreSearch.onCreate() called!");
	super.onCreate(savedInstanceState);
	setContentView(R.layout.store_search);

	addListenerOnButton();

  }
  
  public void addListenerOnButton() {

	radioSearchGroup = (RadioGroup) findViewById(R.id.radioSearch);
	btnSearch = (Button) findViewById(R.id.btnSearch);

	btnSearch.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {

		        // get selected radio button from radioGroup
			int selectedId = radioSearchGroup.getCheckedRadioButtonId();

			// find the radiobutton by returned id
		        radioSearchButton = (RadioButton) findViewById(selectedId);

			Toast.makeText( StoreSearchActivity.this, radioSearchButton.getText(), Toast.LENGTH_SHORT).show();

		}

	});

  }

}
