package com.draws.route;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.draw.route.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    int AUTOCOMPLETE_REQUEST_CODE = 1;
    EditText sourceAddress, destinationAddress;
    Button btnRoute;
    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.PHONE_NUMBER);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sourceAddress       = findViewById(R.id.sourceAddress);
        destinationAddress  = findViewById(R.id.destinationAddress);
        btnRoute            = findViewById(R.id.btnRoute);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.api_key), Locale.US);
        }
        sourceAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,fields) .build(MainActivity.this);
                startActivityForResult(intent, 100);
            }
        });

        destinationAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(MainActivity.this);
                startActivityForResult(intent, 101);
            }
        });

        btnRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String google_url = "http://maps.google.com/maps?saddr="+sourceAddress.getText().toString()+"&daddr="+destinationAddress.getText().toString();
                    String uri = String.format(Locale.ENGLISH, google_url, "Where the party is at");

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    // Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+latitude+","+longitude+"&daddr=28.6531,77.4280"));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);
                }catch (Exception ev){
                    System.out.print(ev.getMessage());
                }
            }
        });
    }

    // A place has been received; use requestCode to track the request.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // retrive the data by using getPlace() method.
            Place place = Autocomplete.getPlaceFromIntent(data);
            Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());
            sourceAddress.setText(place.getAddress().toString());  // TODO - CHANGE THIS TO getAddress()
        } else if (requestCode == 101 && resultCode == RESULT_OK) {
            // retrive the data by using getPlace() method.
            Place place = Autocomplete.getPlaceFromIntent(data);
            Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());
            destinationAddress.setText(place.getAddress().toString());
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            // TODO: Handle the error.
            Log.e("Tag", status.getStatusMessage());
            Toast.makeText(MainActivity.this, "GPS not working..", Toast.LENGTH_LONG).show();

        } else if (resultCode == RESULT_CANCELED) {
            // The user canceled the operation.
            Toast.makeText(MainActivity.this, "The user canceled the operation.", Toast.LENGTH_LONG).show();
        }


    }

}