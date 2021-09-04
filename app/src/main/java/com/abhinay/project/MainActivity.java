package com.abhinay.project;

import static android.provider.MediaStore.Images.Media.getBitmap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ImageView;
import android.provider.MediaStore;
import android.view.View.MeasureSpec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText ProductNameEdt, ProductPriceEdt, ProductDescriptionEdt;

    private Button submitBtn, viewProductsBtn, BSelectImage;

    private String ProductName, ProductPrice, ProductDescription;

    private FirebaseFirestore db;

    private Uri filePath;

    ImageView IVPreviewImage;

    Bitmap bitmap;

    int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        ProductNameEdt = findViewById(R.id.idEdtProductName);
        ProductDescriptionEdt = findViewById(R.id.idEdtProductDescription);
        ProductPriceEdt = findViewById(R.id.idEdtProductPrice);
        BSelectImage = findViewById(R.id.BSelectImage);
        submitBtn = findViewById(R.id.idBtnSubmit);
        viewProductsBtn = findViewById(R.id.idBtnViewProducts);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);

        BSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
        viewProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ProductView.class);
                startActivity(i);
            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductName = ProductNameEdt.getText().toString();
                ProductDescription = ProductDescriptionEdt.getText().toString();
                ProductPrice = ProductPriceEdt.getText().toString();
                IVPreviewImage = findViewById(R.id.IVPreviewImage);

                bitmap = IVPreviewImage.getDrawingCache();
                if (TextUtils.isEmpty(ProductName)) {
                    ProductNameEdt.setError("Please enter Product Name");
                } else if (TextUtils.isEmpty(ProductDescription)) {
                    ProductDescriptionEdt.setError("Please enter Product Description");
                } else if (TextUtils.isEmpty(ProductPrice)) {
                    ProductPriceEdt.setError("Please enter Product Price");
                } else {
                    addDataToFirestore(ProductName, ProductDescription, ProductPrice, bitmap);
                }
            }
        });
    }

    private void addDataToFirestore(String ProductName, String ProductDescription, String ProductPrice, Bitmap bitmap) {
        CollectionReference dbProducts = db.collection("Products");
        Products Products = new Products(ProductName, ProductDescription, ProductPrice,bitmap);

        dbProducts.add(Products).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(MainActivity.this, "Your Product has been added to Firebase Firestore", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Fail to add Product \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }
    void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    IVPreviewImage.setImageURI(selectedImageUri);
                }
            }
        }
    }
}
