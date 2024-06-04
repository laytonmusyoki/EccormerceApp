package com.example.ecormmerce;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    TextView login;
    TextView register;
    CoordinatorLayout coordinatorLayout;
    EditText usernameInput,emailInput,passwordInput,confirmInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        login=findViewById(R.id.login);
        coordinatorLayout=findViewById(R.id.main);
        usernameInput=findViewById(R.id.username);
        emailInput=findViewById(R.id.email);
        passwordInput=findViewById(R.id.password);
        confirmInput=findViewById(R.id.confirm);
        register=findViewById(R.id.register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=usernameInput.getText().toString();
                String email=emailInput.getText().toString();
                String password=passwordInput.getText().toString();
                String confirm=confirmInput.getText().toString();

                if(TextUtils.isEmpty(username)){
                    usernameInput.setError("Username is required");
                    usernameInput.requestFocus();
                } else if (TextUtils.isEmpty(email)) {
                    emailInput.setError("Email is required");
                    emailInput.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailInput.setError("Please enter a valid email");
                    emailInput.requestFocus();
                }
                else if (TextUtils.isEmpty(password)) {
                    passwordInput.setError("Password is required");
                    passwordInput.requestFocus();
                }
                else if (TextUtils.isEmpty(confirm)) {
                    confirmInput.setError("Confirm field is required");
                    confirmInput.requestFocus();
                }
                else if (!password.equals(confirm)) {
                    passwordInput.setError("Passwords don't match try again!");
                    passwordInput.requestFocus();
                }
                else{
                    register.setText("Registering...");
                    register.setEnabled(false);
                    RegisterData registerData=new RegisterData();
                    registerData.setUsername(username);
                    registerData.setEmail(email);
                    registerData.setPassword(password);

                    apiInterface apiInterface=RetrofitClient.getRetrofitInstance().create(com.example.ecormmerce.apiInterface.class);
                    Call<RegisterResponse> call=apiInterface.postRegisterData(registerData);
                    call.enqueue(new Callback<RegisterResponse>() {
                        @Override
                        public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, ""+response.body().getStatus(), Toast.LENGTH_SHORT).show();
                                if(response.body().getStatus().equals("201")){
                                    showSnackBar(response.body().getSuccess());
                                    Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(RegisterActivity.this, "" + response.body().getSuccess(), Toast.LENGTH_SHORT).show();
                                } else if (response.body().getStatus().equals("400")) {
                                    register.setText("REGISTER");
                                    register.setEnabled(false);
                                    List<messageData> errorMessages=response.body().getMessage();
                                    for(messageData error:errorMessages){
                                        Toast.makeText(RegisterActivity.this, "Error" + error.getNon_field_errors(), Toast.LENGTH_LONG).show();
                                    }
                                }
                                else {
                                    Toast.makeText(RegisterActivity.this, "Something went wrong please try again", Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<RegisterResponse> call, Throwable t) {
                            register.setText("REGISTER");
                            register.setEnabled(true);
                            Toast.makeText(RegisterActivity.this, "Something went wrong " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            private void showSnackBar(String message) {
                Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                View snackbarView = snackbar.getView();
                snackbarView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
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
}