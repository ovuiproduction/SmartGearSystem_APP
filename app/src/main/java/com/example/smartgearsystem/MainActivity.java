package com.example.smartgearsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartgearsystem.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    Button btnGetInfo;
    TextView textViewDisplayResult;

    TextView textViewDisplayResult2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGetInfo = findViewById(R.id.btnGetInfo);
        textViewDisplayResult = findViewById(R.id.textViewDisplayResult);
        textViewDisplayResult2=findViewById(R.id.textViewDisplayResult2);

        btnGetInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfo();
            }
        });
    }
    @SuppressLint("SetTextI18n")
    private void getInfo() {
        String tempUrl = "";
        tempUrl = "https://api.thingspeak.com/channels/<Your_CHANNEL_ID>/feeds.json?api_key=<YOUR_API_KEY>&results=1";
        StringRequest stringRequest = new StringRequest(Request.Method.GET,tempUrl,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                String output = "";
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("feeds");
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObjectFeeds = jsonArray.getJSONObject(i);
                        String description = jsonObjectFeeds.getString("field1");
                        output = output+" "+description;
                    }
                    textViewDisplayResult.setText("Current RPM : "+output);
                    executeSecondRequest();
                }catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public  void onErrorResponse(VolleyError error){
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void executeSecondRequest() {
        String tempUrl2 = "https://api.thingspeak.com/channels/<Your_CHANNEL_ID>/fields/1.json?api_key=<YOUR_API_KEY>&results=1";
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, tempUrl2, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                String output = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("feeds");
                    JSONObject jsonObjectFeeds = jsonArray.getJSONObject(0);
                    String description = jsonObjectFeeds.getString("field1");
                    output = output + " " + description;

                    textViewDisplayResult2.setText("Current Gear : " + output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest2);
    }
}
