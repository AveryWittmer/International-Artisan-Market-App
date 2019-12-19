package com.example.finalartisanmarketapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Random;

public class HomeScreen extends AppCompatActivity {
    public interface DataStatus {
        void DataIsLoaded(ArrayList<Vendor> venList);
    }

    private static final String TAG = "HomeScreen";

    private DatabaseReference mDatabaseRef;

    private ImageView showcaseImage;
    private Button homeButton;
    private Button vendorsButton;
    private TextView vendorName;

    private ArrayList<Vendor> vendorList;
    private Vendor vendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        showcaseImage = findViewById(R.id.showcase_image);
        homeButton = findViewById(R.id.home_button);
        vendorsButton = findViewById(R.id.vendors_button);
        vendorName = findViewById(R.id.vendor_name);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mDatabaseRef.keepSynced(true);
        vendorList = new ArrayList<>();

        getVendorData(new DataStatus() {
            @Override
            public void DataIsLoaded(final ArrayList<Vendor> venList) {
                final int randomIndex = new Random().nextInt(venList.size());
                vendorName.setText(venList.get(randomIndex).vendorName);
                Glide
                        .with(getApplicationContext())
                        .load(Uri.parse(venList.get(randomIndex).vendorLogoUrl))
                        .into(showcaseImage);
                showcaseImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent redirect = new Intent("android.intent.action.VIEW",
                                Uri.parse(venList.get(randomIndex).vendorSiteUrl));
                        startActivity(redirect);
                    }
                });
            }
        });


        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, HomeScreen.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        vendorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, VendorsScreen.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    public void getVendorData(final DataStatus dataStatus) {
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    vendor = new Vendor();
                    String val = child.getValue().toString();

                    String[] vals = val.split(",");

                    String[] keyValPairs = vals[0].split("=", 2);
                    vendor.vendorName = keyValPairs[1];

                    keyValPairs = vals[1].split("=", 2);
                    vendor.vendorSiteUrl = keyValPairs[1];

                    keyValPairs = vals[2].split("=", 2);
                    vendor.vendorLogoUrl = keyValPairs[1].replace("}", "");

                    vendorList.add(vendor);
                }
                dataStatus.DataIsLoaded(vendorList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }
}