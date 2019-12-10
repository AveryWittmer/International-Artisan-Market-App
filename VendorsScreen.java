package com.example.finalartisanmarketapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class VendorsScreen extends AppCompatActivity {

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private FirebaseFirestore firebaseFirestore;

    private Button homeButton;
    private ScrollView vendorScrollView;
    private LinearLayout vendorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendors_screen);

       /* homeButton = findViewById(R.id.home_button);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        vendorScrollView = findViewById(R.id.vendor_scroll_view);
        vendorLayout = findViewById(R.id.vendor_layout);

        // Form URL needs to be in: "https://firebasestorage.googleapis.com/v0/b/artisan-market-app-33706.appspot.com/o/APBakedGoods.jpg?alt=media&token=189f3cd7-1d7c-4193-8e72-1d82cadca271"




        /*final ArrayList<StorageReference> prefixes = new ArrayList<>();
        final ArrayList<StorageReference> items = new ArrayList<>();
        mStorageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference prefix: listResult.getPrefixes()) {
                    //It DOES go into here... fix the arraylist stuff
                    prefixes.add(prefix);
                }
                for (StorageReference item : listResult.getItems())
                    items.add(item);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "This failed to get the list", Toast.LENGTH_LONG).show();
            }
        });*/

        /*final StorageReference imageRef = mStorageRef.child("/APBakedGoods.jpg");

        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //for(StorageReference prefix: prefixes){
                    ImageView imageView = new ImageView(getApplicationContext());
                    imageView.setImageURI(uri);
                    vendorScrollView.addView(vendorLayout);
                //}
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Didn't get the download url", Toast.LENGTH_SHORT).show();
            }
        });

        /*homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VendorsScreen.this, HomeScreen.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });*/
    }
}
