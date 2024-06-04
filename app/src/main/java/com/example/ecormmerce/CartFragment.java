package com.example.ecormmerce;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartFragment extends Fragment {

    List<CartModel> cartModels;

    LinearLayoutManager linearLayoutManager;

    CartAdapter cartAdapter;

    RecyclerView recyclerView;

    ProgressBar progressBar;

    LinearLayout linearLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView=view.findViewById(R.id.recylerview);
        progressBar=view.findViewById(R.id.progressbar);
        linearLayout=view.findViewById(R.id.emptycart);

        androidx.appcompat.widget.Toolbar toolbar=view.findViewById(R.id.toolbar);

        toolbar.setTitle("Cart");
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);


        cartModels=new ArrayList<>();

        fetchCartItems();



        initRecycler();


        return view;
    }

    private void fetchCartItems() {
        LoginResponse loginResponse=new LoginResponse();
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String access=sharedPreferences.getString("ACCESS_TOKEN",null);
        String AccessToken="Bearer "+access;
        apiInterface apiInterface=RetrofitClient.getRetrofitInstance().create(com.example.ecormmerce.apiInterface.class);
        Call<CartList> call=apiInterface.getCartProducts(AccessToken);
        call.enqueue(new Callback<CartList>() {
            @Override
            public void onResponse(Call<CartList> call, Response<CartList> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().equals("200")){
                        List<CartModel> fetchedCartProducts=response.body().getCartProducts();
                        cartModels.clear();
                        cartModels.addAll(fetchedCartProducts);
                        progressBar.setVisibility(View.GONE);
                        cartAdapter.notifyDataSetChanged();
                    }
                    else {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }
                if(cartModels.isEmpty()){
                    progressBar.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else {
                    linearLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<CartList> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRecycler() {
        linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        cartAdapter=new CartAdapter(cartModels);
        recyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
    }
}