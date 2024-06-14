package com.example.ecormmerce;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;

    Adapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<ProductModel> productModels;



    TextView desktops,all,laptops,phones,accessories;

    ProgressBar progressBar;
    FrameLayout frame_layout;

    SearchView searchView;

    SwipeRefreshLayout swipeRefreshLayout;

    BottomNavigationView bottomNavigationView;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);



        frame_layout=findViewById(R.id.frame_layout);
        bottomNavigationView=findViewById(R.id.bottom);


        bottomNavigationView.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) this);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new HomeFragment()).commit();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setCategoryData(String category) {
        List<ProductModel> categoryData=new ArrayList<>();
        for (ProductModel items:productModels){
            if(items.getCategories().contains(category)){
                categoryData.add(items);
            }
        }
        if(categoryData.isEmpty()){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
        else{
            adapter.setCategory(categoryData);
        }
    }



    private void searchData(String text) {
        List<ProductModel> foundData=new ArrayList<>();
        for(ProductModel item:productModels){
            if(item.getName().toLowerCase().contains(text.toLowerCase())){
                foundData.add(item);
            }
        }
        if (foundData.isEmpty()){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
        else{
            adapter.setNewData(foundData);
        }
    }






    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        Fragment selectedFragment=null;
        if(id==R.id.home){
            selectedFragment=new HomeFragment();
        } else if (id==R.id.profile) {
            selectedFragment=new ProfileFragment();
        }
        else if (id==R.id.cart) {
            selectedFragment=new CartFragment();
        }
        if(selectedFragment !=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,selectedFragment).commit();
        }
        return true;
    }


}



