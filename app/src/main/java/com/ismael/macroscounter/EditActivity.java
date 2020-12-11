package com.ismael.macroscounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class EditActivity extends AppCompatActivity {

    private Button saveButtonEditP;
    private Button deleteButtonEditP;
    private EditText foodNameEditTextEditP;
    private EditText grMlEditTextEditP;
    private EditText kcalEditTextEditP;
    private EditText proteinEditTextEditP;
    private EditText carbsEditTextEditP;
    private EditText fatsEditTextEditP;

    private String foodId;
    private String timeOfEating;

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        saveButtonEditP = findViewById(R.id.saveButtonEditP);
        deleteButtonEditP = findViewById(R.id.deleteButtonEditP);
        foodNameEditTextEditP = findViewById(R.id.foodNameEditTextEditP);
        grMlEditTextEditP = findViewById(R.id.grMlEditTextEditP);
        kcalEditTextEditP = findViewById(R.id.kcalEditTextEditP);
        proteinEditTextEditP = findViewById(R.id.proteinEditTextEditP);
        carbsEditTextEditP = findViewById(R.id.carbsEditTextEditP);
        fatsEditTextEditP = findViewById(R.id.fatsEditTextEditP);

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        foodId = bundle.getString("id");
        foodNameEditTextEditP.setText(bundle.getString("foodName"));
        timeOfEating = bundle.getString("timeOfEating");
        grMlEditTextEditP.setText(Integer.toString(bundle.getInt("grMl")));
        kcalEditTextEditP.setText(Integer.toString(bundle.getInt("kcal")));
        proteinEditTextEditP.setText(Integer.toString(bundle.getInt("protein")));
        carbsEditTextEditP.setText(Integer.toString(bundle.getInt("carbs")));
        fatsEditTextEditP.setText(Integer.toString(bundle.getInt("fats")));

        // Spinner element
        final Spinner spinner = findViewById(R.id.spinnerEditP);

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

        // Set default value of the spinner
        switch (timeOfEating) {
            case "Desayuno":
                spinner.setSelection(0);
                break;
            case "Almuerzo":
                spinner.setSelection(1);
                break;
            case "Cena":
                spinner.setSelection(2);
                break;
            default:
        }

        // Inicio de Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        saveButtonEditP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (foodNameEditTextEditP.getText().toString().equals("")) {
                    foodNameEditTextEditP.setError("Nombre requerido");
                } else if (kcalEditTextEditP.getText().toString().equals("")) {
                    kcalEditTextEditP.setError("Kcal requeridas");
                } else if (proteinEditTextEditP.getText().toString().equals("")) {
                    proteinEditTextEditP.setError("Proteinas requeridas");
                } else if (carbsEditTextEditP.getText().toString().equals("")) {
                    carbsEditTextEditP.setError("Carbohidratos requeridos");
                } else if (fatsEditTextEditP.getText().toString().equals("")) {
                    fatsEditTextEditP.setError("Grasas requeridos");
                } else { // Se añade la comida a la base de datos
                    Food food = new Food();
                    food.setId(foodId);
                    food.setNombre(foodNameEditTextEditP.getText().toString());
                    food.setTimeOfEating(spinner.getSelectedItem().toString());
                    food.setGrMl(Integer.parseInt(grMlEditTextEditP.getText().toString()));
                    food.setKcal(Integer.parseInt(kcalEditTextEditP.getText().toString()));
                    food.setProtein(Integer.parseInt(proteinEditTextEditP.getText().toString()));
                    food.setCarbs(Integer.parseInt(carbsEditTextEditP.getText().toString()));
                    food.setFats(Integer.parseInt(fatsEditTextEditP.getText().toString()));

                    // Se elimina el objeto de la bbdd antes de meter el actualizado
                    databaseReference.child("User").child(mAuth.getUid()).child(timeOfEating).child("Food").child(foodId).removeValue();
                    // Se mete el nuevo actualizado
                    databaseReference.child("User").child(mAuth.getUid()).child(spinner.getSelectedItem().toString()).child("Food").child(foodId).setValue(food);

                    Toast.makeText(EditActivity.this, "Guardado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        deleteButtonEditP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se elimina el objeto de la bbdd
                databaseReference.child("User").child(mAuth.getUid()).child(timeOfEating).child("Food").child(foodId).removeValue();
                finish();
            }
        });
    }
}