package com.example.iwill;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    TextView btn;
    EditText inputUsername,inputPassword, inputEmail, inputConfirmPassword;
    Button btnregister;
    SharedPreferences sp;
    String nameStr, emailStr;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextView btn = findViewById(R.id.alreadyHaveAccount);
        inputUsername = findViewById(R.id.inputName2);
        inputEmail = findViewById(R.id.inputEmail1);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        mAuth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(RegisterActivity.this);
        btnregister = findViewById(R.id.btnRegister);

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCredentials();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));

            }

        });
    }

    private void checkCredentials() {
        String name = inputUsername.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String confirmPassword = inputConfirmPassword.getText().toString();

        if(name.isEmpty()) {
            showError(inputUsername,"Enter Your Name!");
        }
        else if (email.isEmpty() || !email.contains("@"))
        {
            showError(inputEmail,"Email is not valid!!");
        }
        else if (password.isEmpty() || password.length()<8 || password.contains("\"^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$\""))
        {
            showError(inputPassword,"Password must be 8 characters and contain at least one number, one uppercase letter, one lowercase letter, and one special character if you want to!!");
        }
        else if (confirmPassword.isEmpty() || !confirmPassword.equals(password))
        {
            showError(inputConfirmPassword,"Password does not match!");
        }
        else
        {
           mLoadingBar.setTitle("Registration");
           mLoadingBar.setMessage("Please wait, while check your credentials!!");
           mLoadingBar.setCanceledOnTouchOutside(false);
           mLoadingBar.show();


           mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   if (task.isSuccessful()){
                       Toast.makeText(RegisterActivity.this, "Successfully Registered!!", Toast.LENGTH_SHORT).show();

                       mLoadingBar.dismiss();
                       Intent intent = new Intent (RegisterActivity.this,MainActivity.class);
                       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                       startActivity(intent);

                   }
                   else
                   {
                       Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                   }
               }
           });
        }
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}