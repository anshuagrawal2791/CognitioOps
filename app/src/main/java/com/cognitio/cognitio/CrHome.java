package com.cognitio.cognitio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Map;

public class CrHome extends AppCompatActivity {
    SharedPreferences profile,prefs;
    SharedPreferences.Editor editor,editor2;
    Button button;
    TextView city,zone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cr_home);
        prefs = getApplicationContext().getSharedPreferences(
                Constants.LAUNCH_TIME_PREFERENCE_FILE, Context.MODE_PRIVATE);
        editor = prefs.edit();
        profile = getApplicationContext().getSharedPreferences(Constants.PROFILE_PREFERENCE_FILE,Context.MODE_PRIVATE);
        editor2 = profile.edit();

        button = (Button)findViewById(R.id.button);
        city = (TextView)findViewById(R.id.city_tv);
        zone = (TextView)findViewById(R.id.zone_tv);

        city.setText(profile.getString(Constants.CITY,"default"));
        Constants.ZONES_MAP2.put("N","North");
        Constants.ZONES_MAP2.put("S","South");
        Constants.ZONES_MAP2.put("E","East");
        Constants.ZONES_MAP2.put("W","West");
        Constants.ZONES_MAP2.put("C","Central");
        zone.setText(Constants.ZONES_MAP2.get(profile.getString(Constants.ZONE,"default")).toString());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CrHome.this,AddSchool.class));

            }
        });

        Map<String,?> keys = profile.getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.e("map values",entry.getKey() + ": " +
                    entry.getValue().toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==R.id.action_logout){
            editor.putBoolean(Constants.FIRST_TIME,false);
            editor.commit();
            editor2.clear();
            editor2.commit();
//            LoginManager.getInstance().logOut();
            Intent intent = new Intent(CrHome.this,FirstScreen.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
