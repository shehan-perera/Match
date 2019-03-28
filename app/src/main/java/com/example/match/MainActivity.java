package com.example.match;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.CYAN;
import static android.location.LocationManager.GPS_PROVIDER;

public class MainActivity extends AppCompatActivity  {

    // Create a string for items list -- THESE ITEMS NEED TO COME FROM THE SQL DATABASE
    String items1 [] = new String [] {"Apple", "Orange", "Banana", "Grapes", "Fruits", "dfasd", "dfuahsdkjf", "ajksdhfjkalds"};
    String items2 [] = new String [] {"drake", "future", "chipotle", "library", "SEL", "Bolz", "Lab"};

    // Create 2 Variables to Temporarily store the Information on User Location and Intersts.
    public ArrayList<String> InterestArray = new ArrayList<String>();
    public ArrayList<String> LocationArray = new ArrayList<String>();
    public String radius;
    public String userID;
    public String Lat;
    public String Long;

    private LocationManager lm;

    // Create a DataBase Reference Object
    DatabaseReference personalDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the database reference
        FirebaseApp.initializeApp(this);
        personalDatabase = FirebaseDatabase.getInstance().getReference("User DataBase");


        // Link the XML items
        ListView listView_interests = (ListView) findViewById(R.id.listview_interests);
        ListView listView_locations = (ListView) findViewById(R.id.listview_locations);

        // Create an Array Adapter
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items1); // items is the stuff going inside the list.
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items2);

        // Link the list view with the adapter
        listView_interests.setAdapter(adapter1);
        listView_locations.setAdapter(adapter2);

        // make the items clickable ( INTERESTS ) & COLLECT the information

        listView_interests.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            // When the item is clicked it will call this method - this method should add this information to firebase.
            int interest_counter = 5;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // once this counter reaches 5 stop adding items to the array
                if (interest_counter != 0){
                    // Change the color of the clicked item.
                    int c = Color.CYAN;
                    view.setBackgroundColor(c);

                    // Store the Users Selections.
                    InterestArray.add(items1[position]);


                    // Make a Toast to Keep the User Informed.
                    Toast.makeText(MainActivity.this, "Item " + items1[position] + " Added!", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MainActivity.this, "You have " + counter + " choices left", Toast.LENGTH_SHORT).show();
                    interest_counter--;
                } else {
                    Toast.makeText(MainActivity.this, "Select Locations Below", Toast.LENGTH_LONG).show();
                }

            }


        });

        // make the locations clickable ( LOCATIONS ) & COLLECT the information

        listView_locations.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            // When the item is clicked it will call this method - this method should add this information to firebase.
            int location_counter = 5;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // once this counter reaches 5 stop adding items to the array
                if (location_counter != 0){
                    // Change the color of the clicked item.
                    int c = Color.CYAN;
                    view.setBackgroundColor(c);

                    // Store the Users Selections.
                    LocationArray.add(items2[position]);

                    // Make a Toast to Keep the User Informed.
                    Toast.makeText(MainActivity.this, "Item " + items2[position] + " Added!", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MainActivity.this, "You have " + counter + " choices left", Toast.LENGTH_SHORT).show();
                    location_counter--;
                } else {
                    Toast.makeText(MainActivity.this, "Select Travel Radius Below", Toast.LENGTH_LONG).show();
                }

            }
        });


        // Set up a listener on the SEND button.
        // Get The Information for the Travel Radius. This step should also get the Lat/Long Information.
        Button send = (Button) findViewById(R.id.findMatch);


        send.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                // What the SEND button does goes in here.
                // First We need the Radius Information
                EditText travel_radius = (EditText) findViewById(R.id.enter_travelRadius);
                radius = travel_radius.getText().toString().trim();

                // Mock lat and long
                lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Check Permissions Now
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
                } else {
                    // permission has been granted, continue as usual
                    final Location location = lm.getLastKnownLocation(GPS_PROVIDER);
                    Lat = String.valueOf(location.getLatitude());
                    Long = String.valueOf(location.getLongitude());
                }
                //Location location = lm.getLastKnownLocation(GPS_PROVIDER); // ************************************* FIX THIS LINE

                //Lat = String.valueOf(location.getLatitude());
                //Long = String.valueOf(location.getLongitude());
                // Next We Need to Obtain the User Id from faceBook Authentication.
                // UserID = ...

                // We have the infomation of the interests and locations above. InterestArray, LocationArray.

                // Now we can put it all together and store it in our UserProfile and send it to the database.

                if (LocationArray.isEmpty() || InterestArray.isEmpty() || radius.isEmpty()){
                    Toast.makeText(MainActivity.this, "One or More Field Empty", Toast.LENGTH_LONG).show();
                } else {
                    userID = personalDatabase.push().getKey(); // This is a unique Id Per person
                    UserProfile MyInformation = new UserProfile(userID, InterestArray, LocationArray, radius, Lat, Long);
                    personalDatabase.child(userID).setValue(MyInformation);
                    Toast.makeText(getApplicationContext(), "Finding Friend...", Toast.LENGTH_LONG).show();

                } // WORKING UP TO THIS POINT WHERE I WRITE INFO TO FIREBASE***********

                // When SEND is pressed, the information should be matched Up and we Should get back a bunch of users which we can display in a seperate activity.
                Query query = FirebaseDatabase.getInstance().getReference("User DataBase").orderByChild("InterestArray").equalTo("Fruits");

                query.addListenerForSingleValueEvent(valueEventListener);

            }

        });

    }

    ValueEventListener valueEventListener = new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // What happens when your database changes goes here.
            if (dataSnapshot.exists()) {
                // dataSnapshot is the "issue" node with all children with id 0
                for (DataSnapshot matches : dataSnapshot.getChildren()) {


                }
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

}
