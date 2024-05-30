package com.example.ecormmerce;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    public List<ProductModel> productModels;

    public Adapter(List<ProductModel> productModels){
        this.productModels=productModels;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_page,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        int productImage=productModels.get(position).getImage();
        String productName=productModels.get(position).getName();
        String productPrice=productModels.get(position).getPrice();

        holder.setData(productName,productImage,productPrice);
    }

    @Override
    public int getItemCount() {
        return productModels != null ? productModels.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,price;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            price=itemView.findViewById(R.id.amount);
            image=itemView.findViewById(R.id.image);
        }

        public void setData(String productName, int productImage, String productPrice) {
            name.setText(productName);
            price.setText(productPrice);
            image.setImageResource(productImage);
        }
    }
}
