package com.frannie.showsaholic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

/**
 * Created by Francesca on 15/06/2015.
 */
public class EpisodeScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.episode_screen);
        savedInstanceState.getParcelable("current_ep");

        Calendar rightNow = Calendar.getInstance();
        int today= rightNow.DAY_OF_MONTH;


        Button button = (Button) findViewById(R.id.button2GoogleCal);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //@TODO: Intent to Calendar
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setData(CalendarContract.Events.CONTENT_URI);
                intent.putExtra(CalendarContract.Events.TITLE, "Watch this episode!");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

}
