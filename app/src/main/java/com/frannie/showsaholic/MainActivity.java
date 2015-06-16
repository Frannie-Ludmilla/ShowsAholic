package com.frannie.showsaholic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity {
    protected String selectedSeries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText mySeries   = (EditText)findViewById(R.id.editTextMain);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Log.v("EditText", mySeries.getText().toString());
                selectedSeries = mySeries.getText().toString();
                /*Bundle data = new Bundle();
                data.putString("selectedSeries", selectedSeries);
                Intent in = new Intent(MainActivity.this, SearchScreen.class);
                in.putExtras(data);*/
                Intent in = new Intent(MainActivity.this, SearchScreen.class);
                in.putExtra("selectedSeries",selectedSeries);
                startActivity(in);
                //@TODO: Send Data bundle to next fragment Fragment Manager or Fragment Transaction
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
