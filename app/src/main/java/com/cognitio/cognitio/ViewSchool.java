package com.cognitio.cognitio;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewSchool extends AppCompatActivity {
    String id;
    VolleySingleton volleysingleton;
    RequestQueue requestQueue;
    EditText name,email,password,password2,phone,city,comments,visit,city2,status,cr_name,cr_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_school);
        id = getIntent().getStringExtra("id");
        volleysingleton= VolleySingleton.getinstance(this);
        requestQueue = volleysingleton.getrequestqueue();
//        Toast.makeText(this,getIntent().getStringExtra("id"),Toast.LENGTH_SHORT).show();
        name = (EditText)findViewById(R.id.name);
        email = (EditText)findViewById(R.id.email);
        phone = (EditText)findViewById(R.id.phone);
        password= (EditText)findViewById(R.id.password);
        password2 = (EditText)findViewById(R.id.password2);
        city = (EditText)findViewById(R.id.city);
        city2 = (EditText)findViewById(R.id.city2);
        comments = (EditText)findViewById(R.id.comments);
        visit = (EditText)findViewById(R.id.visit);

        status = (EditText) findViewById(R.id.status);
        cr_name = (EditText) findViewById(R.id.cr_name);
        cr_phone = (EditText) findViewById(R.id.cr_phone);
        name.setKeyListener(null);
        email.setKeyListener(null);
        phone.setKeyListener(null);
        password.setKeyListener(null);
        password2.setKeyListener(null);
        city.setKeyListener(null);
        city2.setKeyListener(null);
        comments.setKeyListener(null);
        visit.setKeyListener(null);
        status.setKeyListener(null);
        cr_name.setKeyListener(null);
        cr_phone.setKeyListener(null);
        getSchoolById(id);




    }

    private void getSchoolById(final String id) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching...");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GET_SCHOOL_BY_ID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject resp = new JSONObject(response);
                    Log.e("get_schools",resp.toString());

                    school s;
                    JSONObject current = resp;
                    try {

                            s = new school(current.getString("id"), current.getString("cr"), current.getString("zone"), current.getString("name"), current.getString("phone"), current.getString("city"), current.getString("email"), current.getString("address"), current.getString("person_met"), current.getString("designation"), current.getString("status"), current.getString("visit"),  current.getString("comments") , current.getString("cr_name"), current.getString("cr_phone"));
                        }catch (JSONException e){
                            s = new school(current.getString("id"), current.getString("cr"), current.getString("zone"), current.getString("name"), current.getString("phone"), current.getString("city"), current.getString("email"), current.getString("address"), current.getString("person_met"), current.getString("designation"), current.getString("status"), current.getString("visit")," ", current.getString("cr_name"), current.getString("cr_phone"));

                        }

                    name.setText(s.getName());
                    email.setText(s.getEmail());
                    phone.setText(s.getPhone());
                    city2.setText(s.getCity());
                    city.setText(s.getAddress());
                    password.setText(s.getPerson_met());
                    password2.setText(s.getDesignation());
                    comments.setText(s.getComments());
                    visit.setText(s.getVisit());
                    status.setText(s.getStatus());
                    cr_name.setText(s.getCr_name());
                    cr_phone.setText(s.getCr_phone());
                    Log.e("school",s.getName());

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

                params.put("id",id);


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
