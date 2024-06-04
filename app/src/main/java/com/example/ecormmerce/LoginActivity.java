package com.example.ecormmerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    TextView register;
    String message;
    TextView login;

    EditText usernameInput,passwordInput;

    CoordinatorLayout coordinatorLayout;

    public static final String Tag="LoginActivity";
    public String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        register=findViewById(R.id.register);
        login=findViewById(R.id.login);
        usernameInput=findViewById(R.id.username);
        coordinatorLayout=findViewById(R.id.main);
        passwordInput=findViewById(R.id.password);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=usernameInput.getText().toString();
                String password=passwordInput.getText().toString();


                if(TextUtils.isEmpty(username)){
                    usernameInput.setError("Username is required");
                    usernameInput.requestFocus();
                    showSnackBar("Username is required");

                }
                else if (TextUtils.isEmpty(password)) {
                    passwordInput.setError("Password is required");
                    passwordInput.requestFocus();
                    showSnackBar("Password is required");
                }
                else{
                    login.setText("Loading...");
                    LoginData loginData=new LoginData();
                    loginData.setUsername(username);
                    loginData.setPassword(password);
                    apiInterface apiInterface=RetrofitClient.getRetrofitInstance().create(com.example.ecormmerce.apiInterface.class);
                    Call<LoginResponse> call=apiInterface.getLogged(loginData);
                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if(response.isSuccessful() && response.body() !=null){
                                if(response.body().status.equals("200")){
                                    token=response.body().getAccess();
                                    saveAccessToken(token);
                                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else if (response.body().status.equals("400")) {
                                    login.setText("Login");
                                    showSnackBar(response.body().getError());
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Log.d(Tag,t.getMessage());
                            login.setText("Login");
                            showSnackBar("Login Failed " + t.getMessage());
                        }
                    });
                }
            }

            private void showSnackBar(String message) {
                Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                View snackbarView = snackbar.getView();
                snackbarView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.warning)));
                snackbarView.animate();
                snackbar.show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void saveAccessToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ACCESS_TOKEN", token);
        editor.apply();
    }

}