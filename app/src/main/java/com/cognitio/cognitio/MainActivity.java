package com.cognitio.cognitio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    SharedPreferences profile,prefs;
    SharedPreferences.Editor editor,editor2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getApplicationContext().getSharedPreferences(
                Constants.LAUNCH_TIME_PREFERENCE_FILE, Context.MODE_PRIVATE);
        editor = prefs.edit();
        profile = getApplicationContext().getSharedPreferences(Constants.PROFILE_PREFERENCE_FILE,Context.MODE_PRIVATE);
        editor2 = profile.edit();

        Map<String,?> keys = profile.getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.e("map values",entry.getKey() + ": " +
                    entry.getValue().toString());
        }

        if (!prefs.getBoolean(Constants.FIRST_TIME,false)) {
            // <---- run your one time code here
            Intent intent = new Intent(this,FirstScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);


        }
        else if(profile.getString(Constants.TYPE,"defualt").equals("cr")){

            Intent intent = new Intent(this,CrHome.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
        else if(profile.getString(Constants.TYPE,"defualt").equals("ma")){
            Intent intent = new Intent(this,MaHome.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
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
            Intent intent = new Intent(MainActivity.this,FirstScreen.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
