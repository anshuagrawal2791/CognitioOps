package com.cognitio.cognitio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Map;

public class CrLogin extends AppCompatActivity {
    EditText email,password;
    Button login;
    TextView signup;
    boolean error;
    SharedPreferences profile,launch_time;
    SharedPreferences.Editor editor,editor2;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cr_login);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);
        signup = (TextView)findViewById(R.id.signup);
        profile = getSharedPreferences(Constants.PROFILE_PREFERENCE_FILE,MODE_PRIVATE);
        editor = profile.edit();
        launch_time = getSharedPreferences(Constants.LAUNCH_TIME_PREFERENCE_FILE,MODE_PRIVATE);
        editor2 = launch_time.edit();

        volleySingleton = VolleySingleton.getinstance(this);
        requestQueue = volleySingleton.getrequestqueue();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CrLogin.this,UserDetails.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error = false;
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
                if(password.getText().toString().equals(""))
                {
                    password.setError("Required");
                    error = true;
                }

                if(error == false) {
                    loginUser(email.getText().toString(),password.getText().toString(),"0");
                }
            }
        });



    }




    private void loginUser(final String email_string, final String password_string,final String fb) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Logging in...");
        dialog.show();
        Log.e("aa",email_string);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject resp = new JSONObject(response);
                    Log.e("login",resp.toString());
                    if(resp.getBoolean("res")){
                        editor.putString(Constants.EMAIL,resp.getString(Constants.EMAIL));
                        editor.putString(Constants.ZONE,resp.getString(Constants.ZONE));
                        editor.putString(Constants.ID,resp.getString(Constants.ID));
                        editor.putString(Constants.NAME,resp.getString(Constants.NAME));
                        editor.putString(Constants.PHONE,resp.getString(Constants.PHONE));
                        editor.putString(Constants.TYPE,"cr");

                        editor.putString(Constants.CITY,resp.getString(Constants.CITY));
//                        editor.putString(Constants.SCHOOL,resp.getString(Constants.SCHOOL));
//                        editor.putString(Constants.PIN,resp.getString(Constants.PIN));
//                        editor.putString(Constants.ADDRESS_LINE1,resp.getString(Constants.ADDRESS_LINE1));
//                        editor.putString(Constants.ADDRESS_LINE2,resp.getString(Constants.ADDRESS_LINE2));
//                        editor.putString(Constants.ADDRESS,resp.getString(Constants.ADDRESS));
//                        editor.putString(Constants.LONGITUDE,resp.getJSONArray("location").getString(0));
//                        editor.putString(Constants.LATITUDE,resp.getJSONArray("location").getString(1));

                        JSONArray images = resp.getJSONObject("imagesS3").getJSONArray("name");

                        if(images.length()>0)
                            editor.putString(Constants.PROFILE_PIC,images.get(images.length()-1).toString());
                        else editor.putString(Constants.PROFILE_PIC," ");
//                        Log.e("image",resp.getJSONObject(Constants.IMAGESS3).getJSONArray(Constants.NAME).get(0).toString());
                        editor.commit();
                        editor2.putBoolean(Constants.FIRST_TIME,true);
                        editor2.commit();
                        Toast.makeText(getApplicationContext(),resp.getString("response"),Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);



                    }
                    else{
                        Toast.makeText(CrLogin.this,resp.getString("response"),Toast.LENGTH_LONG).show();
                    }
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
                Map<String,String> params = new HashMap<String, String>();
                params.put(Constants.EMAIL,email_string);
                params.put(Constants.PASSWORD,password_string);
                params.put("fb",fb);
                Log.e("aa",params.toString());
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
