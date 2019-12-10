package com.example.finalartisanmarketapp;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.DragAndDropPermissions;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.model.GlideUrl;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import org.json.JSONArray;
import org.json.JSONObject;

public class HomeScreen extends AppCompatActivity {
    private static final String TAG = "HomeScreen";

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private ImageView showcaseImage;
    private Button homeButton;
    private Button vendorsButton;

    private ArrayList<Drawable> vendorLogos;
    private String[] vendorNames;
    private ArrayList<String> vendorWebsites;
    private JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        showcaseImage = findViewById(R.id.showcase_image);
        homeButton = findViewById(R.id.home_button);
        vendorsButton = findViewById(R.id.vendors_button);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();


        // Form URL needs to be in: "https://firebasestorage.googleapis.com/v0/b/artisan-market-app-33706.appspot.com/o/APBakedGoods.jpg?alt=media&token=189f3cd7-1d7c-4193-8e72-1d82cadca271"

        /*StorageReference imageRef = mStorageRef.child("/APBakedGoods.jpg");
        StorageReference imageRef = mStorageRef.child("https://firebasestorage.googleapis.com/v0/b/artisan-market-app-33706.appspot.com/o/APBakedGoods.jpg?alt=media&token=189f3cd7-1d7c-4193-8e72-1d82cadca271"
        );*/
        try {
            vendorNames = getAssets().list("logos");
            for (int j = 0; j < vendorNames.length; j++) {
            }
        } catch (IOException e) {

            e.printStackTrace();
        }

        vendorLogos = GetLogos();

        try {
            //get json data loaded properly, manipulate to line up urls with
            //vendor logos so that they'll redirect right onClick of ImageView
            //jsonObject = new JSONObject();
            jsonArray = new JSONArray(loadJSONFromAsset());
            final int randomIndex = new Random().nextInt(vendorLogos.size());
            for (int i = 0; i < jsonArray.length(); i++) {
                if (jsonArray.getJSONObject(i).getString("vendorName")
                        .equals(vendorNames[randomIndex]
                                .substring(0, vendorNames[randomIndex].length()-4))) {
                    showcaseImage.setImageDrawable(vendorLogos.get(randomIndex));
                    /*vendorWebsites.add(jsonArray.getJSONObject(i).getString("vendorWebsite"));
                    showcaseImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent externalIntent = new Intent("android.intent.action.VIEW",
                                    Uri.parse(vendorWebsites.get(randomIndex)));
                        }
                    });*/
                }
            }
        } catch (org.json.JSONException e) {
            e.printStackTrace();
            Log.i(TAG, "Oops! There goes the JSON!");
        }

        /*final ArrayList<StorageReference> prefixes = new ArrayList<>();
        final ArrayList<StorageReference> items = new ArrayList<>();
        mStorageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for(StorageReference prefix: listResult.getPrefixes()){
                    //It DOES go into here... fix the arraylist stuff
                    prefixes.add(prefix);
                }
                for(StorageReference item: listResult.getItems())
                    items.add(item);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "This failed to get the list", Toast.LENGTH_LONG).show();
            }
        });*/

        /*StorageMetadata metadata = new StorageMetadata.Builder()
                .setCustomMetadata("websiteUrl",
                        "https://www.apbakedgoods.com/?fbclid=IwAR2LBqY0o_TaYuD0lcXdnDa1phgJD7HrRwEjDWmmJHu_nYE400lK7H0CeSw").build();
        imageRef.updateMetadata(metadata);*/

        /*imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide
                        .with(getApplicationContext())
                        .load(uri)
                        .into(showcaseImage);
                Toast.makeText(getApplicationContext(), "This is the download uri: " + uri, Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Didn't get the download url", Toast.LENGTH_SHORT).show();
            }
        });*/

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

    ArrayList<Drawable> GetLogos() {
        ArrayList<Drawable> logos = new ArrayList<>();
        try {
            AssetManager assetManager = getAssets();
            String[] logoNames = assetManager.list("logos");
            for (String logoName : logoNames) {
                InputStream inputStream = assetManager.open("logos/" + logoName);
                Drawable logo = Drawable.createFromStream(inputStream, null);
                logos.add(logo);
            }
        } catch (IOException e) {
            Log.i(TAG, "IOException with getting logo list");
            logos = null;
        }
        return logos;
    }

    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream inputStream = getAssets().open("vendor_data.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
