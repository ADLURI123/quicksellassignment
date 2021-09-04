package com.abhinay.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductRVAdapter extends RecyclerView.Adapter<ProductRVAdapter.ViewHolder> {

    private ArrayList<Products> ProductsArrayList;
    private Context context;

    public ProductRVAdapter(ArrayList<Products> ProductsArrayList, Context context) {
        this.ProductsArrayList = ProductsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.product_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductRVAdapter.ViewHolder holder, int position) {
        Products Products = ProductsArrayList.get(position);
        holder.ProductNameTV.setText(Products.getProductName());
        holder.ProductPriceTV.setText(Products.getProductPrice());
        holder.ProductDescTV.setText(Products.getProductDescription());
        holder.ProductImage.setImageBitmap(Products.getBitmap());
    }

    @Override
    public int getItemCount() {
        return ProductsArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView ProductNameTV;
        private final TextView ProductPriceTV;
        private final TextView ProductDescTV;
        private final ImageView ProductImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ProductNameTV = itemView.findViewById(R.id.idTVProductName);
            ProductPriceTV = itemView.findViewById(R.id.idTVProductPrice);
            ProductDescTV = itemView.findViewById(R.id.idTVProductDescription);
            ProductImage = itemView.findViewById(R.id.IVPreviewImage);
        }
    }
}

