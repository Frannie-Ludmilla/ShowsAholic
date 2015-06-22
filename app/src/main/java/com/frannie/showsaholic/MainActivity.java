package com.frannie.showsaholic;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    protected String selectedSeries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        final EditText mySeries   = (EditText)findViewById(R.id.editTextMain);
        mySeries.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        mySeries.setImeOptions(EditorInfo.IME_ACTION_DONE);
        getSupportActionBar().hide();
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setLogo(R.drawable.logo);
        //setSupportActionBar(toolbar);

        //ActionBar actionBar = getActionBar();
        //actionBar.setLogo(R.drawable.title);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Log.v("EditText", mySeries.getText().toString());
                selectedSeries = mySeries.getText().toString();
                selectedSeries= selectedSeries.trim();
                selectedSeries = selectedSeries.replaceAll(" ", "%20");
                Intent in = new Intent(MainActivity.this, SearchScreen.class);
                in.putExtra("selectedSeries",selectedSeries);
                startActivity(in);
            }
        });

        mySeries.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Log.v("EditText", mySeries.getText().toString());
                    selectedSeries = mySeries.getText().toString();
                    selectedSeries = selectedSeries.trim();
                    selectedSeries = selectedSeries.replaceAll(" ", "%20");
                    Intent in = new Intent(MainActivity.this, SearchScreen.class);
                    in.putExtra("selectedSeries", selectedSeries);
                    startActivity(in);
                }
                return false;
            }
        });
    }


    /*public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }*/
}
