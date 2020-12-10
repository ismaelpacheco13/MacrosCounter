package com.ismael.macroscounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ismael.macroscounter.model.User;

import java.util.UUID;

public class AuthActivity extends AppCompatActivity {

    private Button signUpButton;
    private Button logInButton;
    private EditText emailEditText;
    private EditText passwordEditText;

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        signUpButton = findViewById(R.id.signUpButton);
        logInButton = findViewById(R.id.logInButton);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        // Inicio de Firebase
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        // Setup
        setup();
    }

    private void setup() {
        setTitle("Autenticación");
        mAuth = FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!emailEditText.getText().toString().isEmpty()) && (!passwordEditText.getText().toString().isEmpty())) {
                    mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Si el registro es correcto, se actualiza el UI y se guarda el usuario en la base de datos
                                User user = new User();
                                user.setId(mAuth.getUid());
                                user.setEmail(emailEditText.getText().toString());
                                user.setPassword(passwordEditText.getText().toString());
                                databaseReference.child("User").child(user.getId()).setValue(user);
                                goToHomeActivity();
                            } else {
                                // Si el inicio de sesión falla se muestra un toast
                                Toast.makeText(AuthActivity.this, "Registro fallido", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!emailEditText.getText().toString().isEmpty()) && (!passwordEditText.getText().toString().isEmpty())) {
                    mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Si el registro es correcto, se actualiza el UI
                                goToHomeActivity();
                            } else {
                                // Si el inicio de sesión falla se muestra un toast
                                Toast.makeText(AuthActivity.this, "Inicio de sesión fallido", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void goToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("email", emailEditText.getText().toString());
        startActivity(intent);
    }
}