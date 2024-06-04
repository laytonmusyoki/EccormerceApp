package com.example.ecormmerce;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    public List<ProductModel> productModels;
    private static final String BASE_URL = "https://shop-qttp.onrender.com"; // Base URL of your server

    public Adapter(List<ProductModel> productModels){
        this.productModels = productModels;
    }



    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_page, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        ProductModel product = productModels.get(position);
        String id=product.getId();
        String productImage = product.getImage();
        String productName = product.getName();
        String productPrice = product.getPrice();
        String action="add";

        holder.setData(productName, BASE_URL + productImage, productPrice);
        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.addToCart.setText("Adding...");
                CartID cartID=new CartID();
                cartID.setAction(action);
                cartID.setProductId(id);
                SharedPreferences sharedPreferences=v.getContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                String access=sharedPreferences.getString("ACCESS_TOKEN",null);
                String AccessToken="Bearer "+access;
                apiInterface apiInterface=RetrofitClient.getRetrofitInstance().create(com.example.ecormmerce.apiInterface.class);
                Call<AddItemResponse> call=apiInterface.postCartId(AccessToken,cartID);
                call.enqueue(new Callback<AddItemResponse>() {
                    @Override
                    public void onResponse(Call<AddItemResponse> call, Response<AddItemResponse> response) {
                        if(response.isSuccessful() && response.body() !=null){
                            if(response.body().getSuccess().equals("200")){
                                holder.addToCart.setText("Add To Cart");
                                Toast.makeText(v.getContext(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AddItemResponse> call, Throwable t) {
                        Toast.makeText(v.getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return productModels != null ? productModels.size() : 0;
    }

    public void setNewData(List<ProductModel> foundData) {
        this.productModels=foundData;
        notifyDataSetChanged();
    }

    public void setCategory(List<ProductModel> categoryData) {
        this.productModels=categoryData;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price,addToCart;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.amount);
            image = itemView.findViewById(R.id.image);
            addToCart=itemView.findViewById(R.id.addToCart);
        }

        public void setData(String productName, String productImageUrl, String productPrice) {
            name.setText(productName);
            price.setText(productPrice);
            Glide.with(image.getContext())
                    .load(productImageUrl)// Add an error image
                    .into(image);
        }
    }
}
