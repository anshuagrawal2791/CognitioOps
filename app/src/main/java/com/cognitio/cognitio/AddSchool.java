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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddSchool extends AppCompatActivity {
    EditText name,email,password,password2,phone,city,comments,visit,city2;
    Spinner status;
    Button next;
    boolean error;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    SharedPreferences profile,launch_time;
    SharedPreferences.Editor editor, editor2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_school);

        name = (EditText)findViewById(R.id.name);
        email = (EditText)findViewById(R.id.email);
        phone = (EditText)findViewById(R.id.phone);
        password= (EditText)findViewById(R.id.password);
        password2 = (EditText)findViewById(R.id.password2);
        city = (EditText)findViewById(R.id.city);
        city2 = (EditText)findViewById(R.id.city2);
        comments = (EditText)findViewById(R.id.comments);
        visit = (EditText)findViewById(R.id.visit);

        status = (Spinner)findViewById(R.id.status);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.classes2, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status.setAdapter(adapter);

        next = (Button)findViewById(R.id.next);

        volleySingleton = VolleySingleton.getinstance(this);
        requestQueue = volleySingleton.getrequestqueue();

        profile = getSharedPreferences(Constants.PROFILE_PREFERENCE_FILE,MODE_PRIVATE);
        editor = profile.edit();
        launch_time = getSharedPreferences(Constants.LAUNCH_TIME_PREFERENCE_FILE,MODE_PRIVATE);
        editor2=launch_time.edit();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error=false;
                if(name.getText().toString().equals("")) {
                    name.setError("Enter Name");
                    error=true;
                }


                if(email.getText().toString().equals(""))
                {
                    email.setError("Required");
                    error = true;
                }
                if(phone.getText().toString().equals(""))
                {
                    phone.setError("Required");
                    error = true;
                }
                if(city.getText().toString().equals("")){
                    city.setError("Required");
                    error=true;
                }
                if(city2.getText().toString().equals("")){
                    city2.setError("Required");
                    error=true;
                }
                if(password.getText().toString().equals("")){
                    password.setError("Required");
                    error=true;
                }
                if(password2.getText().toString().equals("")){
                    password2.setError("Required");
                    error=true;
                }

//                if(visit.getText().toString().equals("")){
//                    visit.setError("Required");
//                    error=true;
//                }
//                if(!visit.getText().toString().equals("")){
//                    if(Integer.parseInt(visit.getText().toString())<=0){
//                        visit.setError("Invalid");
//                        error=true;
//                    }
//                }

                if(error==false){
                    registerUser();
                }
            }
        });



    }

    private void registerUser() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Sending...");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ADD_SCHOOL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject resp = new JSONObject(response);
                    Log.e("login",resp.toString());
                    if(resp.getBoolean("res")){

                        Toast.makeText(getApplicationContext(),resp.getString("response"),Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), CrHome.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);



                    }
                    else{
                        Toast.makeText(AddSchool.this,resp.getString("response"),Toast.LENGTH_LONG).show();
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
                Map<String,String> params = new HashMap<>();
                params.put(Constants.ID,profile.getString(Constants.ID,"default"));
                params.put(Constants.ZONE,profile.getString(Constants.ZONE,"default"));
                params.put("cr_name",profile.getString(Constants.NAME,"default"));
                params.put("cr_phone",profile.getString(Constants.PHONE,"default"));
                params.put("name",name.getText().toString());
                params.put("email",email.getText().toString());
                params.put("phone",phone.getText().toString());
                params.put("address",city.getText().toString());
                params.put("city",city2.getText().toString());
                params.put("person_met",password.getText().toString());
                params.put("designation",password2.getText().toString());
                params.put("comments",comments.getText().toString());
                params.put("visit","1");
                params.put("status",status.getSelectedItem().toString());

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
