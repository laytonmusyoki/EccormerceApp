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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;
import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    public List<CartModel> cartModels;
    public String id;
    public String action;

    SwipeRefreshLayout swipeRefreshLayout;
    private static final String BASE_URL = "https://shop-qttp.onrender.com"; // Base URL of your server

    public CartAdapter(List<CartModel> cartModels){
        this.cartModels=cartModels;
    }
    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_design,parent,false);
        swipeRefreshLayout=view.findViewById(R.id.swiper);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        String cartId=cartModels.get(position).getId();
        String cartName=cartModels.get(position).getName();
        String cartPrice=cartModels.get(position).getPrice();
        String cartCategory=cartModels.get(position).getCategories();
        String cartTotal=cartModels.get(position).getCartTotal();
        String cartImage=cartModels.get(position).getImage();
        String cartQuantity=cartModels.get(position).getQuantity();
        holder.setData(cartName,cartPrice,cartTotal,BASE_URL+cartImage,cartCategory,cartQuantity);


        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action = "remove";
                id = cartId;
                updateCart(v.getContext(), id, action);
            }
        });

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action = "add";
                id = cartId;
                updateCart(v.getContext(), id, action);
            }
        });
    }

    private void updateCart(Context context, String id, String action) {
        CartID cartID = new CartID();
        cartID.setAction(action);
        cartID.setProductId(id);
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String access = sharedPreferences.getString("ACCESS_TOKEN", null);
        String accessToken = "Bearer " + access;
        apiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(com.example.ecormmerce.apiInterface.class);
        Call<AddItemResponse> call = apiInterface.postCartId(accessToken, cartID);
        call.enqueue(new Callback<AddItemResponse>() {
            @Override
            public void onResponse(Call<AddItemResponse> call, Response<AddItemResponse> response) {
                if(response.isSuccessful() && response.body() !=null){
                    if(response.body().getSuccess().equals("200")){
                        Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddItemResponse> call, Throwable t) {
                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return cartModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,price,total,category,items,minus,add;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            category=itemView.findViewById(R.id.category);
            price=itemView.findViewById(R.id.price);
            total=itemView.findViewById(R.id.total);
            image=itemView.findViewById(R.id.image);
            items=itemView.findViewById(R.id.quantity);
            minus=itemView.findViewById(R.id.minus);
            add=itemView.findViewById(R.id.add);
        }


        public void setData(String cartName, String cartPrice, String cartTotal, String s,String cartCategory,String cartQuantity) {
            name.setText(cartName);
            price.setText(cartPrice+"/item");
            items.setText(cartQuantity);
            category.setText(cartCategory);
            total.setText(cartTotal);
            Glide.with(image.getContext())
                    .load(s)// Add an error image
                    .into(image);
        }
    }
}
