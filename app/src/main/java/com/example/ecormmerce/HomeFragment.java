package com.example.ecormmerce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;

    public String currentCategory=null;

    Adapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<ProductModel> productModels;



    TextView desktops,all,laptops,phones,accessories;

    ProgressBar progressBar;

    SearchView searchView;


    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
       inflater.inflate(R.menu.menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.logout){
            Intent intent=new Intent(getActivity(),LoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Example: Find a view in the fragment's layout
        swipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);
        progressBar=view.findViewById(R.id.progress);
        searchView=view.findViewById(R.id.search);
        accessories=view.findViewById(R.id.accessories);
        desktops=view.findViewById(R.id.desktops);
        phones=view.findViewById(R.id.phones);
        all=view.findViewById(R.id.all);
        laptops=view.findViewById(R.id.laptops);
        recyclerView=view.findViewById(R.id.recyler);

        androidx.appcompat.widget.Toolbar toolbar=view.findViewById(R.id.toolbar);

        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        LoginResponse loginResponse=new LoginResponse();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                searchData(text);
                return true;
            }
        });

        productModels=new ArrayList<>();
        fecthProducts();

        accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCategoryData("Accessories");
                setbackgroundColor(v);
            }
        });



        desktops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCategoryData("Desktop");
                setbackgroundColor(v);
            }
        });

        laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCategoryData("Laptop");
                setbackgroundColor(v);
            }
        });

        phones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCategoryData("Phone");
                setbackgroundColor(v);
            }
        });

        all.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                currentCategory=null;
                fecthProducts();
                setbackgroundColor(v);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentCategory=null;
                fecthProducts();
            }
        });

        initRecycler();


        return view;
    }

    private void setbackgroundColor(View v) {
        v.setBackgroundResource(R.drawable.btn_clicked);
    }

    private void initRecycler() {
        linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        adapter=new Adapter(productModels);
        recyclerView.setAdapter(adapter);
    }

    private void setCategoryData(String category) {
        currentCategory = category;
        fecthProducts();
    }


    private void fecthProducts() {
        apiInterface apiInterface=RetrofitClient.getRetrofitInstance().create(com.example.ecormmerce.apiInterface.class);
        Call<ProductsList> call=apiInterface.getProducts();
        call.enqueue(new Callback<ProductsList>() {
            @Override
            public void onResponse(Call<ProductsList> call, Response<ProductsList> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<ProductModel> fetchedProducts = response.body().getProducts();
                    if (currentCategory != null) {
                        List<ProductModel> filteredProducts = new ArrayList<>();
                        for (ProductModel product : fetchedProducts) {
                            if (product.getCategories().equalsIgnoreCase(currentCategory)) {
                                filteredProducts.add(product);
                            }
                        }
                        productModels.clear();
                        productModels.addAll(filteredProducts);
                        if(productModels.isEmpty()){
                            Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        productModels.clear();
                        productModels.addAll(fetchedProducts);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "Failed to fetch products: " + response.message(), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ProductsList> call, Throwable t) {
                Toast.makeText(getActivity(), "layton"+ t.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }



    private void searchData(String text) {
        List<ProductModel> foundData=new ArrayList<>();
        for(ProductModel item:productModels){
            if(item.getName().toLowerCase().contains(text.toLowerCase())){
                foundData.add(item);
            }
        }
        if (foundData.isEmpty()){
            Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
        }
        else{
            adapter.setNewData(foundData);
        }
    }

}
