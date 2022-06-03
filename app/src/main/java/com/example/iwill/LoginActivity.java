package com.example.iwill;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextView btn;
    EditText inputEmail,inputPassword;
    Button btnLogin;
    private FirebaseAuth mAuth;
    ProgressDialog mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView btn = findViewById(R.id.Backtologin);
        inputEmail = findViewById(R.id.inputName2);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnreset);
        mAuth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(LoginActivity.this);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCredentials();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }


    private void checkCredentials() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (email.isEmpty() || !email.contains("@"))
        {
            showError(inputEmail,"Email is not valid!!");
        }
        else if (password.isEmpty() || password.length()<8 || password.contains("\"^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$\""))
        {
            showError(inputPassword,"Password must be 8 characters and contain at least one number, one uppercase letter, one lowercase letter, and one special character if you want to !!");
        }
        else
        {
            mLoadingBar.setTitle("Login");
            mLoadingBar.setMessage("Please wait while check your credentials!!");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                                       @Override
                                                                                       public void onComplete(@NonNull Task<AuthResult> task) {
                                                                                           if (task.isSuccessful()) {
                                                                                               mLoadingBar.dismiss();
                                                                                               Intent intent = new Intent(LoginActivity.this, LoginLaunch1.class);
                                                                                               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                               startActivity(intent);



                                                                                           }
                                                                                       }
                                                                                   });
        }

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, LoginLaunch1.class));
                }
            });


        }


    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}