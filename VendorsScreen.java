package com.example.finalartisanmarketapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Random;

public class VendorsScreen extends AppCompatActivity {

    public interface DataStatus {
        void DataIsLoaded(ArrayList<Vendor> venList);
    }

    private static final String TAG = "VendorsScreen";

    private DatabaseReference mDatabaseRef;
    private Button homeButton;
    private ArrayList<Vendor> vendorList;
    private Vendor vendor;
    private ScrollView vendorScrollView;
    private LinearLayout vendorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendors_screen);

        homeButton = findViewById(R.id.home_button);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        vendorScrollView = findViewById(R.id.vendor_scroll_view);
        vendorLayout = findViewById(R.id.vendor_layout);
        vendorList = new ArrayList<>();


        getVendorData(new HomeScreen.DataStatus() {
            @Override
            public void DataIsLoaded(final ArrayList<Vendor> venList) {
                for (Vendor ven : venList) {
                    final Vendor venInList = ven;
                    ImageView logo = new ImageView(getApplicationContext());
                    logo.setPadding(10, 10, 10, 10);
                    Glide
                            .with(getApplicationContext())
                            .load(Uri.parse(ven.vendorLogoUrl))
                            .into(logo);
                    logo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent redirect = new Intent("android.intent.action.VIEW",
                                    Uri.parse(venInList.vendorSiteUrl));
                            startActivity(redirect);
                        }
                    });

                    vendorLayout.addView(logo);
                }
            }
        });


        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VendorsScreen.this, HomeScreen.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    public void getVendorData(final HomeScreen.DataStatus dataStatus) {
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
