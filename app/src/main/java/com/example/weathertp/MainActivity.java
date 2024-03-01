package com.example.weathertp;

import static android.service.controls.ControlsProviderService.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.weathertp.databinding.ActivityMainBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private String name;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        requestQueue = Volley.newRequestQueue(this);

        binding.search.setOnClickListener(v -> {
            fetchData();
        });
    }

    public void fetchData() {
        name = binding.nameCity.getText().toString();
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + binding.input.getText().toString() + "&appid=1543d69662cb97f8eff33954d2b62ff8&units=metric";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String nameCity = response.getString("name");
                    JSONArray weather = response.getJSONArray("weather");
                    String description = weather.getJSONObject(0).getString("description");
                    double temp = response.getJSONObject("main").getDouble("temp");

                    JSONObject object = weather.getJSONObject(0);
                    String icon = object.getString("icon");
                    String iconWeather = "https://openweathermap.org/img/wn/" + icon + "@2x.png";
                    Glide.with(MainActivity.this).load(iconWeather).into(binding.imgMeteo);

                    binding.nameCity.setText(nameCity);
                    binding.main.setText(description);
                    binding.temperature.setText((int) temp + " °C");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "This city does not exist", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
