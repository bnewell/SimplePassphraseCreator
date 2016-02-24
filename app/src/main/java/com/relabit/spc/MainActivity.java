package com.relabit.spc;

import java.util.ArrayList;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    PasswordGenerator pw;
    
    ListView passphraseList;
    
    /* (non-Javadoc)
     * 
     * Application start
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        pw = new PasswordGenerator();
        ArrayList<String> passphrases = (ArrayList<String>) pw.getPassphraseList(1000);
        
        passphraseList = (ListView) findViewById(R.id.passphraseList);
        
        passphraseList.setAdapter((ListAdapter) new ArrayAdapter<String>(MainActivity.this,
        				android.R.layout.simple_list_item_1, passphrases));
        
        //
        // COPY PASSPHRASE TO CLIPBOARD
        passphraseList.setClickable(true);
        passphraseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @SuppressLint("NewApi")
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        	// Get selected passphrase from list
            Object o = passphraseList.getItemAtPosition(position);
            // Copy selected passsphrase to clipboard
            int apiVersion = android.os.Build.VERSION.SDK_INT;
            if (apiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB){
                 android.content.ClipboardManager clipboard =  (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE); 
                    ClipData clip = ClipData.newPlainText("Passphrase", o.toString());
                    clipboard.setPrimaryClip(clip); 
            } else{
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager)getSystemService(CLIPBOARD_SERVICE); 
                clipboard.setText("Text to Copy");
            }
            Toast.makeText(getApplicationContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
          }
        });
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                finish();
                startActivity(getIntent());
                return true;
            case R.id.menu_settings: 
            	startActivity(new Intent(MainActivity.this, AboutActivity.class));
             	return true;

        }
        return super.onOptionsItemSelected(item);
    }
    
}
