package com.cognitio.cognitio;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.StringSearch;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MaHome extends AppCompatActivity {
    SharedPreferences profile,prefs;
    SharedPreferences.Editor editor,editor2;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    RecyclerView recycler;
    ma_recycler_adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ma_home);

        prefs = getApplicationContext().getSharedPreferences(
                Constants.LAUNCH_TIME_PREFERENCE_FILE, Context.MODE_PRIVATE);
        editor = prefs.edit();
        profile = getApplicationContext().getSharedPreferences(Constants.PROFILE_PREFERENCE_FILE,Context.MODE_PRIVATE);
        editor2 = profile.edit();
        volleySingleton = VolleySingleton.getinstance(this);
        requestQueue = volleySingleton.getrequestqueue();
        recycler = (RecyclerView)findViewById(R.id.recycler);
        adapter = new ma_recycler_adapter(this);
        recycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recycler.setAdapter(adapter);

        
        getSchools();



        Map<String,?> keys = profile.getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.e("map values",entry.getKey() + ": " +
                    entry.getValue().toString());
        }
    }

    private void getSchools() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching...");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GET_SCHOOL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONArray resp = new JSONArray(response);
                    Log.e("get_schools",resp.toString());
                    ArrayList<school> s = new ArrayList<>();
                    for(int i=0;i<resp.length();i++){
                        JSONObject current = resp.getJSONObject(i);
//                        Log.e("comments",current.getString("comments"));
                        try {
                            s.add(new school(current.getString("id"), current.getString("cr"), current.getString("zone"), current.getString("name"), current.getString("phone"), current.getString("city"), current.getString("email"), current.getString("address"), current.getString("person_met"), current.getString("designation"), current.getString("status"), current.getString("visit"),  current.getString("comments") , current.getString("cr_name"), current.getString("cr_phone")));
                        }catch (JSONException e){
                            s.add(new school(current.getString("id"), current.getString("cr"), current.getString("zone"), current.getString("name"), current.getString("phone"), current.getString("city"), current.getString("email"), current.getString("address"), current.getString("person_met"), current.getString("designation"), current.getString("status"), current.getString("visit")," ", current.getString("cr_name"), current.getString("cr_phone")));

                        }
                    }
                    adapter.addAll(s);
//                    if(resp.getBoolean("res")){
//
////                        Toast.makeText(getApplicationContext(),resp.getString("response"),Toast.LENGTH_LONG).show();
////                        Intent intent = new Intent(getApplicationContext(), CrHome.class);
////                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                        startActivity(intent);
//
//
//
//                    }
//                    else{
//                        Toast.makeText(MaHome.this,resp.getString("response"),Toast.LENGTH_LONG).show();
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e("err",error.toString());

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                JSONArray z ;
                ArrayList<String> a = new ArrayList<>();
                 a.addAll(profile.getStringSet(Constants.ZONES, null));
                z = new JSONArray(a);


                params.put("zones",z.toString());


                Log.e("params",params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
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
            Intent intent = new Intent(MaHome.this,FirstScreen.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
