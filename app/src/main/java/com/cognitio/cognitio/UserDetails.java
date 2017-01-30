package com.cognitio.cognitio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserDetails extends AppCompatActivity {
    EditText name,email,password,password2,phone,city;
    Spinner zone;
    Button next;
    boolean error;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    SharedPreferences profile,launch_time;
    SharedPreferences.Editor editor, editor2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        name = (EditText)findViewById(R.id.name);
        email = (EditText)findViewById(R.id.email);
        phone = (EditText)findViewById(R.id.phone);
        password= (EditText)findViewById(R.id.password);
        password2 = (EditText)findViewById(R.id.password2);
        city = (EditText)findViewById(R.id.city);

        zone = (Spinner)findViewById(R.id.zone);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.classes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        zone.setAdapter(adapter);

        next = (Button)findViewById(R.id.next);
        Constants.ZONES_MAP.put("North","N");
        Constants.ZONES_MAP.put("South","S");
        Constants.ZONES_MAP.put("East","E");
        Constants.ZONES_MAP.put("West","W");
        Constants.ZONES_MAP.put("Central","C");

//        for (Object entry : Constants.ZONES_MAP.values()) {
//            Log.e("aa",entry.toString());
//            // ...
//        }

        volleySingleton = VolleySingleton.getinstance(this);
        requestQueue = volleySingleton.getrequestqueue();

        profile = getSharedPreferences(Constants.PROFILE_PREFERENCE_FILE,MODE_PRIVATE);
        editor = profile.edit();
        launch_time = getSharedPreferences(Constants.LAUNCH_TIME_PREFERENCE_FILE,MODE_PRIVATE);
        editor2=launch_time.edit();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error=false;
                if(name.getText().toString().equals("")) {
                    name.setError("Enter Name");
                    error=true;
                }

                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches())
                {
                    email.setError("Invalid Email");
                    //email.setText("");
                    error = true;
                }
                if(email.getText().toString().equals(""))
                {
                    email.setError("Required");
                    error = true;
                }
                if(city.getText().toString().equals("")){
                    city.setError("Required");
                    error=true;
                }
                if(!password2.getText().toString().equals(password.getText().toString()))
                {
                    password2.setError("Passwords don't match");
                    password2.setText("");
                    error = true;
                }
                if(password.getText().length()<5)
                {
                    password.setError("Minimum 5 characters");
                    password.setText("");
                    password2.setText("");
                }
                if(phone.getText().length()<10)
                {
                    phone.setError("Invalid Mobile Number");
                    error=true;
                }
                if(error==false){
                    registerUser();
                }


            }
        });



    }
    private void registerUser() {
//        final String name = profile.getString(Constants.NAME,"default");
//        final String email = profile.getString(Constants.EMAIL,"default");
//        final String phone = profile.getString(Constants.PHONE,"default");
//        final String password = profile.getString(Constants.PASSWORD,"default");
//        final String gender = profile.getString(Constants.GENDER,"default");
//        final String class2 = profile.getString(Constants.CLASS,"default");
//        final String profilePic = profile.getString(Constants.PROFILE_PIC," ");
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Signing up...");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.REGISTER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject resp = new JSONObject(response);
                    Log.e("resp",response);
                    if(resp.getBoolean("res")){
                        editor.putString(Constants.ID,resp.getString(Constants.ID));
                        editor.putString(Constants.NAME,name.getText().toString());
                        editor.putString(Constants.EMAIL,email.getText().toString());
                        editor.putString(Constants.PASSWORD,password.getText().toString());
                        editor.putString(Constants.PHONE,phone.getText().toString());
                        editor.putString(Constants.ZONE,Constants.ZONES_MAP.get(zone.getSelectedItem().toString()).toString());
                        editor.putString(Constants.TYPE,"cr");

                        editor.putString(Constants.CITY,city.getText().toString());

                        editor.commit();
                        editor2.putBoolean(Constants.FIRST_TIME,true);
                        editor2.commit();
                        Intent intent = new Intent(getApplicationContext(), CrHome.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        Toast.makeText(getApplicationContext(),resp.getString("response"),Toast.LENGTH_LONG).show();

                    }
                    else{
                        Toast.makeText(UserDetails.this,resp.getString("response"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(UserDetails.this,"error",Toast.LENGTH_LONG).show();
                Log.e("error",error.toString());

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put(Constants.CITY,city.getText().toString());

                params.put(Constants.NAME,name.getText().toString());
                params.put(Constants.EMAIL,email.getText().toString());
                params.put(Constants.PASSWORD, password.getText().toString());
                params.put(Constants.PHONE, phone.getText().toString());
                params.put(Constants.ZONE,Constants.ZONES_MAP.get(zone.getSelectedItem().toString()).toString());
//                params.put(Constants.GENDER,gender);
//                if(!profilePic.matches(" "))
//                    params.put(Constants.PROFILE_PIC,profilePic);
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
}
