package com.example.ecormmerce;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Adapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<ProductModel> productModels;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        androidx.appcompat.widget.Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        productModels=new ArrayList<>();
        productModels.add(new ProductModel("Laptop","Lenovo 360","rwe","30000","Yes",R.drawable.desktop));
        productModels.add(new ProductModel("Laptop","Lenovo 360","rwe","30000","Yes",R.drawable.laptop));
        productModels.add(new ProductModel("Laptop","Lenovo 360","rwe","30000","Yes",R.drawable.laptop));
        productModels.add(new ProductModel("Laptop","Lenovo 360","rwe","30000","Yes",R.drawable.laptop));
        productModels.add(new ProductModel("Laptop","Lenovo 360","rwe","30000","Yes",R.drawable.desk));
        initRecycler();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initRecycler() {
        recyclerView=findViewById(R.id.recyler);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapter=new Adapter(productModels);
        recyclerView.setAdapter(adapter);
    }

}


