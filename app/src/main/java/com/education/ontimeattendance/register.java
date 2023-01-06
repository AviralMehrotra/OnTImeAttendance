package com.education.ontimeattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity implements View.OnClickListener {

    private TextView registerUser;
    private EditText editFullName, editEmail, editPassword, editConPassword;
    private TextView loginNow;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Window window = register.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(register.this, R.color.orange));


        mAuth = FirebaseAuth.getInstance();

        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        editFullName= (EditText) findViewById(R.id.fullname);
        editEmail= (EditText) findViewById(R.id.email);
        editPassword= (EditText) findViewById(R.id.password);
        editConPassword= (EditText) findViewById(R.id.conPassword);

        loginNow= (TextView) findViewById(R.id.loginNow);
        loginNow.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(register.this, Login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loginNow:
                startActivity(new Intent(this, Login.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
            case R.id.registerUser:
                registerUser();
                startActivity(new Intent(this, Login.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
        }
    }

    private void registerUser(){
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String conPassword = editConPassword.getText().toString().trim();
        String fullname = editFullName.getText().toString().trim();

        if(fullname.isEmpty()){
            editFullName.setError("Full Name is required !");
            editFullName.requestFocus();
            return;
        }

        if(email.isEmpty()){
            editEmail.setError("Email Id is required !");
            editEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editEmail.setError("Enter a valid Email Id !");
            editEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editPassword.setError("Password is necessary !");
            editPassword.requestFocus();
            return;
        }

        if(password.length()<8){
            editPassword.setError("Password should consist of 8 characters or more !");
            editPassword.requestFocus();
            return;
        }

        if(conPassword.isEmpty()){
            editConPassword.setError("Please confirm your password !");
            editConPassword.requestFocus();
            return;
        }

        else if(!password.equals(conPassword)){
            Toast.makeText(register.this, "Passwords don't match !", Toast.LENGTH_SHORT).show();
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(fullname, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(register.this, "Registration Completed !", Toast.LENGTH_LONG).show();
                                            }
                                            else {
                                                Toast.makeText(register.this, "Registration Failed ! Please try Again !", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                            }
                            else {
                                Toast.makeText(register.this, "Registration Failed ! Please try again !", Toast.LENGTH_LONG).show();
                            }
                    }
                });

    }
}