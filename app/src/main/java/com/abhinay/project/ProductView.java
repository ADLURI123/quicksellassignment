package com.abhinay.project;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProductView extends AppCompatActivity {

    private RecyclerView ProductRV;
    private ArrayList<Products> ProductsArrayList;
    private ProductRVAdapter ProductRVAdapter;
    private FirebaseFirestore db;
    ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

        ProductRV = findViewById(R.id.idRVProducts);
        loadingPB = findViewById(R.id.idProgressBar);

        db = FirebaseFirestore.getInstance();

        ProductsArrayList = new ArrayList<>();
        ProductRV.setHasFixedSize(true);
        ProductRV.setLayoutManager(new LinearLayoutManager(this));

        ProductRVAdapter = new ProductRVAdapter(ProductsArrayList, this);

        ProductRV.setAdapter(ProductRVAdapter);

        db.collection("Products").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {

                            loadingPB.setVisibility(View.GONE);
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {

                                Products c = d.toObject(Products.class);
                                ProductsArrayList.add(c);
                            }
                            ProductRVAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(ProductView.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProductView.this, "Fail to get the data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}