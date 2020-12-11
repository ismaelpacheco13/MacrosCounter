package com.ismael.macroscounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ismael.macroscounter.model.Food;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddActivity extends AppCompatActivity {

    private String email = "";

    private Button saveButton;
    private EditText foodNameEditText;
    private EditText grMlEditText;
    private EditText kcalEditText;
    private EditText proteinEditText;
    private EditText carbsEditText;
    private EditText fatsEditText;

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.statusBarColor));
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        email = bundle.getString("email");

        // Inicio de Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        foodNameEditText = findViewById(R.id.foodNameEditText);
        grMlEditText = findViewById(R.id.grMlEditText);
        kcalEditText = findViewById(R.id.kcalEditText);
        proteinEditText = findViewById(R.id.proteinEditText);
        carbsEditText = findViewById(R.id.carbsEditText);
        fatsEditText = findViewById(R.id.fatsEditText);

        saveButton = findViewById(R.id.saveButton);

        // Spinner element
        final Spinner spinner = findViewById(R.id.spinner);

        // Spinner click listener
        //spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Desayuno");
        categories.add("Almuerzo");
        categories.add("Cena");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (foodNameEditText.getText().toString().equals("")) {
                    foodNameEditText.setError("Nombre requerido");
                } else if (kcalEditText.getText().toString().equals("")) {
                    kcalEditText.setError("Kcal requeridas");
                } else if (proteinEditText.getText().toString().equals("")) {
                    proteinEditText.setError("Proteinas requeridas");
                } else if (carbsEditText.getText().toString().equals("")) {
                    carbsEditText.setError("Carbohidratos requeridos");
                } else if (fatsEditText.getText().toString().equals("")) {
                    fatsEditText.setError("Grasas requeridos");
                } else { // Se añade la comida a la base de datos
                    Food food = new Food();
                    food.setId(UUID.randomUUID().toString());
                    food.setNombre(foodNameEditText.getText().toString());
                    food.setTimeOfEating(spinner.getSelectedItem().toString());
                    food.setGrMl(Integer.parseInt(grMlEditText.getText().toString()));
                    food.setKcal(Integer.parseInt(kcalEditText.getText().toString()));
                    food.setProtein(Integer.parseInt(proteinEditText.getText().toString()));
                    food.setCarbs(Integer.parseInt(carbsEditText.getText().toString()));
                    food.setFats(Integer.parseInt(fatsEditText.getText().toString()));

                    databaseReference.child("User").child(mAuth.getUid()).child(food.getTimeOfEating()).child("Food").child(food.getId()).setValue(food);

                    Toast.makeText(AddActivity.this, "Guardado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}